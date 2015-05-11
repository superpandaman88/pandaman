package com.panda.setting;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.panda.setting.constant.SettingConstant;
import com.panda.setting.db.DBUtils;
import com.panda.setting.orm.Server;

/**
 * 
 * @author gaopan
 * @version $Id: SettingContentProvider.java, v 0.1 2015-5-11 下午9:27:49 gaopan Exp $
 */
public class SettingContentProvider extends ContentProvider{
    public static final String AUTHORITY = "com.panda.setting";
    public static final String BASEURI = "content://"+AUTHORITY+"/";

    public static final String TAG = "DataProvider";
        
    private DBUtils mDBUtils;
    
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        mDBUtils = DBUtils.getInstance();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        String key = uri.getLastPathSegment();
        ContentResolver contentResolver = getContext().getContentResolver();
        if(key.equalsIgnoreCase(SettingConstant.BIZ_WALLET_URL)){
            return getServer(contentResolver,SettingConstant.WALLET_TYPE);
        }else if(key.equalsIgnoreCase(SettingConstant.BIZ_PASSPORT_URL)){
            return getServer(contentResolver,SettingConstant.PASSPORT_TYPE);
        }else if(key.equalsIgnoreCase(SettingConstant.BIZ_PLUGIN_URL)){
            return getServer(contentResolver,SettingConstant.PLUGIN_TYPE);
        }
        return null;
    }

    private Cursor getServer(ContentResolver contentResolver, String category ) {
        String selection = "Type=? And isSelected='1'";
        String[] selectionArgs = new String[] {category};
        return mDBUtils.getInstance().getHelper().getReadableDatabase().query(Server.class.getSimpleName(),new String[]{"Url"}, selection, selectionArgs, null, null, null, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

}
