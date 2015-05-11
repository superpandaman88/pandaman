package com.panda.setting;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.panda.codescan.MipcaActivityCapture;
import com.panda.setting.db.DBUtils;
import com.panda.setting.orm.Server;

/**
 * 
 * @author gaopan
 * @version $Id: ModifyOrAddActivity.java, v 0.1 2015-5-11 下午9:27:45 gaopan Exp $
 */
public class ModifyOrAddActivity extends Activity implements OnClickListener, OnItemLongClickListener{
    private ListView mListView;

    private DBUtils mDBUtils;

    private String mType;
    private List<Server> mData;
    EditText titleView;
    EditText urlView;
    private final static int SCAN_NAME_CODE = 0x1;
    private final static int SCAN_URL_CODE = 0x2;
    
    private int mSelection;

    private BaseAdapter mAdapter = new BaseAdapter() {

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater()
                    .inflate(android.R.layout.simple_list_item_2, null);
            }
            String str = ((Server) mData.get(position)).getName();
            String url = ((Server) mData.get(position)).getUrl();
            TextView nameView = (TextView) convertView.findViewById(android.R.id.text1);
            nameView.setText(str);
            TextView urlView = (TextView) convertView.findViewById(android.R.id.text2);
            urlView.setText(url);
            return convertView;
        }

        public long getItemId(int position) {
            return position;
        }

        public Object getItem(int position) {
            return mData.get(position);
        }

        public int getCount() {
            if (mData == null)
                return 0;
            else
                return mData.size();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mType = getIntent().getStringExtra("type");
        String title = getIntent().getStringExtra("name");
        setTitle(title);

        setContentView(R.layout.modify_add_server);

        mDBUtils = DBUtils.getInstance();
        loadAllVariables();
    }

    private void loadAllVariables() {
        try {
            mData = mDBUtils.queryServerListByType(mType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mListView = (ListView) findViewById(R.id.server_list);
        mListView.setOnItemLongClickListener(this);
        registerForContextMenu(mListView);
        mListView.setAdapter(mAdapter);

        Button button = (Button) findViewById(R.id.add);
        button.setOnClickListener(this);
    }

    public void onClick(View v) {
        addServer(null,null,false);
    }

    private void startScanCodeActivity(int resultCode) {
        Intent intent = new Intent();
        intent.setClass(this, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, resultCode);
    }
    
    private void addServer(String title,final String origalUrl,final boolean isModify) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.add_one_server, null);
        titleView = (EditText) view.findViewById(R.id.name);
        urlView = (EditText) view.findViewById(R.id.url);
        Button nameQr = (Button) view.findViewById(R.id.name_qr_btn);
        Button urlQr = (Button) view.findViewById(R.id.url_qr_btn);
        nameQr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanCodeActivity(SCAN_NAME_CODE);
            }
        });
        
        urlQr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanCodeActivity(SCAN_URL_CODE);
            }
        });
        
        if(title!=null)
            titleView.setText(title);
        if(origalUrl!=null)
            urlView.setText(origalUrl);
        builder.setView(view);
        builder.setTitle(R.string.add_server);
        builder.setPositiveButton(R.string.sure_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText nameText = (EditText) view.findViewById(R.id.name);
                String name = nameText.getText().toString();
                if (name.length() <= 0) {
                    Toast.makeText(ModifyOrAddActivity.this, R.string.server_name_error,Toast.LENGTH_LONG).show();
                    return;
                }

                EditText urlText = (EditText) view.findViewById(R.id.url);
                String url = urlText.getText().toString();
                Server server = null;
                if (isModify) {
                    server = mDBUtils.queryServiceByTypeAndUrl(mType, origalUrl);
                } else {
                    server = new Server();
                }
                server.setName(name);
                server.setUrl(url);
                server.setType(mType);
                server.setSelected(server.isSelected());
                mDBUtils.addOrUpdateOneServer(server);
                addServer(server);
                mAdapter.notifyDataSetChanged();
            
            }
        });
        builder.show();
    }
    
    private void addServer(Server server){
        if(mData == null || server == null){
            return;
        }
        removeServer(server);
        mData.add(0, server);
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long arg3) {
        mSelection = position;
        createChooseDialog();
        return true;
    }

    private void createChooseDialog(){
        new AlertDialog.Builder(this).setTitle(getString(R.string.operation)).setItems(new String[] { getString(R.string.delete), getString(R.string.modify)}, mOnclickListener).show();
    }
    
    private void removeServer(Server server){
        if(mData== null || server == null){
            return;
        }
        
        for (int location=0;location<mData.size();location++) {
            Server obj = mData.get(location);
            if(obj.getId() == server.getId()) {
                mData.remove(location);
                break;
            }
        }
    }
    
    private DialogInterface.OnClickListener mOnclickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                String url = ((Server) mData.get(mSelection)).getUrl();
                switch (which) {
                    case 0:
                        Server server = mDBUtils.queryServiceByTypeAndUrl(mType, url);
                        mDBUtils.deleteOneServer(server);
//                        mData.remove(server);
                        removeServer(server);
                        mAdapter.notifyDataSetChanged();
                        
                        if(server.isSelected()&&mData.size()>0){
                            Server tempServer = mData.get(0);
                            tempServer.setSelected(true);
                            mDBUtils.updateOneServer(tempServer);
                            
                        }
                        break;
                    case 1:
                        String title = ((Server) mData.get(mSelection)).getName();
                        addServer(title,url,true);
                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        }
    };
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDBUtils.close();
    };
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case SCAN_NAME_CODE:
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                String result = bundle.getString("result");
                if(!TextUtils.isEmpty(result)){
                    titleView.setText(result);
                    titleView.setSelection(result.length());
                }
            }
            break;
        case SCAN_URL_CODE:
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                String result = bundle.getString("result");
                if(!TextUtils.isEmpty(result)){
                    urlView.setText(result);
                    urlView.setSelection(result.length());
                }
            }
            break;
        }
    }   
}
