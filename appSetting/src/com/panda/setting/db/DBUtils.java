package com.panda.setting.db;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.panda.setting.application.SettingApplication;
import com.panda.setting.dao.ServerDao;
import com.panda.setting.orm.Server;

/**
 * 
 * @author gaopan
 * @version $Id: DBUtils.java, v 0.1 2015-5-11 下午9:28:13 gaopan Exp $
 */
public class DBUtils {
    public static final String TAG = DBUtils.class.getSimpleName();
	private DBhelper dbHelper;
	public static DBUtils mInstance;
	
	private DBUtils() {
    }
	
	public static synchronized DBUtils getInstance(){
	    if(mInstance==null){
	        mInstance = new DBUtils();
	    }
	    return mInstance;
	}
	
	//查询数据库 service表的数据
    public List<Server> queryServerList(){
        ServerDao serverDao = new ServerDao();
        try {
            Dao<Server,Integer> dao = getHelper().getDao(Server.class);
			return serverDao.queryServiceList(dao);
		} catch (SQLException e) {
		    Log.e(TAG, e.toString());
		    return null;
		}
    }
    
    //添加数据库service表的数据
    public boolean addOneServer(Server server){
        ServerDao serverDao = new ServerDao();
        try {
            Dao<Server,Integer> dao = getHelper().getDao(Server.class);
			return serverDao.addOneService(dao, server);
		} catch (SQLException e) {
			Log.e(TAG, e.toString());
            return false;
		}
    }
    
    public boolean addOrUpdateOneServer(Server server){
        ServerDao serverDao = new ServerDao();
        try{
            Dao<Server,Integer> dao = getHelper().getDao(Server.class);
            return serverDao.addOrUpdateOneServer(dao, server);
        }catch(SQLException e){
            Log.e(TAG, e.toString());
            return false;
        }
    }
    
    //删除数据库service表的一条数据
    public boolean deleteOneServer(Server server){
        ServerDao serverDao = new ServerDao();
        try {
            Dao<Server,Integer> dao = getHelper().getDao(Server.class);
			return serverDao.deleteOneService(dao, server);
		} catch (SQLException e) {
		    Log.e(TAG, e.toString());
            return false;
		}
    }
    
    public List<Server> queryServerListByType(String type){
        ServerDao serverDao = new ServerDao();
        try {
            Dao<Server,Integer> dao = getHelper().getDao(Server.class);
			return serverDao.queryServiceListByType(dao, type);
		} catch (SQLException e) {
		    Log.e(TAG, e.toString());
            return null;
		}
    }
    
    //更新一个新的记录，如果没有则创建该记录
    public boolean updateOneServer(Server server){
        ServerDao serverDao = new ServerDao();
        try{
            Dao<Server,Integer> dao = getHelper().getDao(Server.class);
            return serverDao.updateOneServer(dao, server);
        }catch(SQLException e){
            Log.e(TAG, e.toString());
            return false;
        }
    }
    
    public Server queryServiceByTypeAndUrl(String type,String url){
        ServerDao serverDao = new ServerDao();
        try{
            Dao<Server,Integer> dao = getHelper().getDao(Server.class);
            return serverDao.queryServiceByTypeAndUrl(dao, type, url);
        }catch(SQLException e){
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
    public synchronized DBhelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(SettingApplication.getInstance(), DBhelper.class);
        }
        return dbHelper;
    }

    public  synchronized void close() {
        OpenHelperManager.releaseHelper();
        dbHelper = null;
    }
}
