package com.panda.setting.application;

import android.app.Application;

import com.panda.setting.db.DBUtils;

/**
 * 
 * @author gaopan
 * @version $Id: SettingApplication.java, v 0.1 2015-5-11 下午9:27:54 gaopan Exp $
 */
public class SettingApplication extends Application{

    private static Application mInstance;
    
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        DBUtils.getInstance();
    }
    
    public static Application getInstance(){
        return mInstance;
    }
}
