package com.panda.setting.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.panda.setting.dao.ServerDao;
import com.panda.setting.initdata.InitData;
import com.panda.setting.orm.Server;

/**
 * 
 * @author gaopan
 * @version $Id: DBhelper.java, v 0.1 2015-5-11 下午9:28:08 gaopan Exp $
 */
public class DBhelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "panda_setting";
	private static final int DATABASE_VERSION = 1;

	public DBhelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource arg1) {
		try {
			TableUtils.createTableIfNotExists(connectionSource, Server.class);
			exceInitData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0,
			ConnectionSource connectionSource, int arg2, int arg3) {
		try {
			TableUtils.dropTable(connectionSource, Server.class, true);
			onCreate(arg0, connectionSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void exceInitData() throws SQLException {
		List<Server> serverLists = InitData.getInstance().getInitServerLists();
		Dao<Server, Integer> dao = null;
		dao = getDao(Server.class);
		if (dao == null || serverLists == null) {
			return;
		}
		for (Server server : serverLists) {
		    ServerDao serverDao = new ServerDao();
	        serverDao.addOneService(dao, server);
		}
	}
}
