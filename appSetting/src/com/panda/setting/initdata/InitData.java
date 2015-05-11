package com.panda.setting.initdata;

import java.util.ArrayList;
import java.util.List;

import com.panda.setting.constant.SettingConstant;
import com.panda.setting.orm.Server;

/**
 * 
 * @author gaopan
 * @version $Id: InitData.java, v 0.1 2015-5-11 下午9:28:18 gaopan Exp $
 */
public class InitData {
	private static InitData mInstance;
	private List<Server> mServerLists;

	private InitData() {
		mServerLists = new ArrayList<Server>();
	}

	public static synchronized InitData getInstance() {
		if (mInstance == null) {
			mInstance = new InitData();
		}
		return mInstance;
	}
	
	public List<Server> getInitServerLists(){
	    Server server = new Server();
	    server.setName(SettingConstant.ONLINE_NAME);
	    server.setType(SettingConstant.WALLET_TYPE);
	    server.setSelected(true);
	    server.setUrl("https://www.x1.com");
//	    server.setId(0);
	    mServerLists.add(server);
	    
	    server = new Server();
        server.setName("debug");
        server.setType(SettingConstant.WALLET_TYPE);
        server.setSelected(false);
        server.setUrl("https://www.xx2.com");
//        server.setId(1);
        mServerLists.add(server);
        
        
	    server = new Server();
	    server.setName(SettingConstant.ONLINE_NAME);
	    server.setType(SettingConstant.PASSPORT_TYPE);
	    server.setSelected(true);
	    server.setUrl("http://www.xxx3.com/passport/");
//	    server.setId(2);
	    mServerLists.add(server);
	    
	    server = new Server();
	    server.setName(SettingConstant.ONLINE_NAME);
	    server.setType(SettingConstant.PLUGIN_TYPE);
	    server.setSelected(true);
	    server.setUrl("http://xx1/config?xxx2");
	    server.setId(3);
	    mServerLists.add(server);
	    
	    return mServerLists;
	}
}   
