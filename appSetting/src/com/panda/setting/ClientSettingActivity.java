package com.panda.setting;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.panda.setting.constant.SettingConstant;
import com.panda.setting.db.DBUtils;
import com.panda.setting.orm.Server;

/**
 * 
 * @author gaopan
 * @version $Id: ClientSettingActivity.java, v 0.1 2015-5-11 下午9:27:38 gaopan Exp $
 */
public class ClientSettingActivity extends PreferenceActivity implements OnPreferenceClickListener,
                                                             OnPreferenceChangeListener {
    private static final String TAG = ClientSettingActivity.class.getSimpleName();
    private List<Server>        mWalletLists;
    private List<Server>        mPassportLists;
    private List<Server>        mPluginLists;
    
    private List<Server>        mEnvironmentLists;
    private DBUtils             mDBUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.panda_config);
//        mDBUtils = new DBUtils(this);
        mDBUtils = DBUtils.getInstance();
        loadAllData();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(SettingConstant.WALLET_TYPE)) {
            saveServerData(preference, mWalletLists, SettingConstant.WALLET_TYPE, newValue);
        } else if (preference.getKey().equals(SettingConstant.PASSPORT_TYPE)) {
            saveServerData(preference, mPassportLists, SettingConstant.PASSPORT_TYPE, newValue);
        } else if (preference.getKey().equals(SettingConstant.PLUGIN_TYPE)) {
            saveServerData(preference, mPluginLists, SettingConstant.PLUGIN_TYPE, newValue);
        }else if(preference.getKey().equalsIgnoreCase(SettingConstant.ENVIRONMENT_TYPE)){
            saveServerData(preference, mEnvironmentLists, SettingConstant.ENVIRONMENT_TYPE, newValue);
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equalsIgnoreCase(SettingConstant.BIZ_WALLET_MAN)) {
            jumpToModifyActivity(SettingConstant.WALLET_TYPE, getString(R.string.biz_wallet_man));
            return true;
        } else if (preference.getKey().equalsIgnoreCase(SettingConstant.BIZ_PASSPORT_MAN)) {
            jumpToModifyActivity(SettingConstant.PASSPORT_TYPE, getString(R.string.biz_passport_man));
            return true;
        } else if (preference.getKey().equalsIgnoreCase(SettingConstant.BIZ_PLUGIN_MAN)) {
            jumpToModifyActivity(SettingConstant.PLUGIN_TYPE, getString(R.string.biz_plugin_man));
            return true;
        }
        return false;
    }

    public void jumpToModifyActivity(String type, String name) {
        Intent intent = new Intent(this, ModifyOrAddActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    private void loadAllData() {
        Preference pre_wallet_man = findPreference("biz_wallet_man");
        pre_wallet_man.setOnPreferenceClickListener(this);

        Preference pre_passport_man = findPreference("biz_passport_man");
        pre_passport_man.setOnPreferenceClickListener(this);

        Preference pre_debug_flag = findPreference("biz_debug_flag");
        pre_debug_flag.setOnPreferenceChangeListener(this);

        Preference pre_plugin_upgrade_man = findPreference("biz_plugin_man");
        pre_plugin_upgrade_man.setOnPreferenceClickListener(this);
        
        Preference pre_version = findPreference("version");
        pre_version.setEnabled(false);
        try {
            PackageInfo mPackageInfo = getPackageManager().getPackageInfo(getPackageName(),
                PackageManager.GET_ACTIVITIES);
            pre_version.setSummary("v" + mPackageInfo.versionName);
        } catch (Exception e) {
            pre_version.setSummary("无效版本");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWalletLists = getServerListByType(SettingConstant.WALLET_TYPE);
        initListPreference(mWalletLists, SettingConstant.WALLET_TYPE);
        mPassportLists = getServerListByType(SettingConstant.PASSPORT_TYPE);
        initListPreference(mPassportLists, SettingConstant.PASSPORT_TYPE);
        mPluginLists = getServerListByType(SettingConstant.PLUGIN_TYPE);
        initListPreference(mPluginLists, SettingConstant.PLUGIN_TYPE);

    }

    private List<Server> getServerListByType(String type) {
        if (mDBUtils == null) {
            return null;
        }
        return mDBUtils.queryServerListByType(type);
    }

    private void initListPreference(List<Server> lists, String type) {
        if (lists == null) {
            return;
        }
        ListPreference biz = (ListPreference) findPreference(type);

        String[] names = new String[lists.size()];
        String[] values = new String[lists.size()];
        Server selectedServer = null;
        for (int i = 0; i < lists.size(); ++i) {
            Server server = (Server) lists.get(i);
            names[i] = server.getName();
            values[i] = server.getUrl();
            if (server.isSelected()) {
                selectedServer = server;
            }
        }
        biz.setEntries(names);
        biz.setEntryValues(values);

        biz.setOnPreferenceChangeListener(this);

        if(selectedServer !=null){
            biz.setValue(selectedServer.getUrl());
            biz.setTitle(selectedServer.getName());
            biz.setSummary(selectedServer.getUrl());
        }
    }

    public void saveServerData(Preference preference, List<Server> servers, String type,
                               Object newValue) {
        for (int i = 0; i < servers.size(); ++i) {
            Server server = servers.get(i);
            if (server == null || server.getUrl() == null) {
                Log.d(TAG, "server==null||server.getUrl()==null");
                continue;
            }
            if (server.getUrl().equals(newValue)) {
                try {
                    preference.setTitle(server.getName());
                    preference.setSummary(server.getUrl());
                    server.setSelected(true);
                    mDBUtils.updateOneServer(server);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (server.isSelected()) {
                server.setSelected(false);
                mDBUtils.updateOneServer(server);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDBUtils.close();
    }
}
