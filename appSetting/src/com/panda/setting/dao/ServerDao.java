package com.panda.setting.dao;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.panda.setting.orm.Server;

/**
 * 
 * @author gaopan
 * @version $Id: ServerDao.java, v 0.1 2015-5-11 下午9:28:03 gaopan Exp $
 */
public class ServerDao {
	
	//查询数据库 service表的数据
	public List<Server> queryServiceList(Dao<Server, Integer> dao) throws SQLException{
		QueryBuilder<Server, Integer> queryBuilder = dao.queryBuilder();
		return queryBuilder.query();
	}
	
	//添加数据库service表的数据
	public boolean addOneService(Dao<Server,Integer> dao,Server server) throws SQLException{
		Server tempServer = (Server)server.clone();
		int rows = dao.create(tempServer);
		if(rows>0){
			return true;
		}
		return false;
	}
	
	public boolean addOrUpdateOneServer(Dao<Server,Integer> dao,Server server) throws SQLException{
	    Server tempServer = (Server)server.clone();
        CreateOrUpdateStatus status = dao.createOrUpdate(tempServer);
        if(status!=null&&status.getNumLinesChanged()>0){
            return true;
        }
        return false;
	}
	//删除数据库service表的一条数据
	public boolean deleteOneService(Dao<Server,Integer> dao,Server server) throws SQLException{
		DeleteBuilder<Server, Integer> deleteBuilder = dao.deleteBuilder();
		int id = server.getId();
		deleteBuilder.where().eq("Id", id);
		int rows = deleteBuilder.delete();
		if(rows>0){
			return true;
		}
		return false;
	}
	
	public boolean updateOneServer(Dao<Server,Integer> dao,Server server) throws SQLException{
        Server tempServer = (Server)server.clone();
        int rows = dao.update(tempServer);
        if(rows>0){
            return true;
        }
        return false;
    
	}
	public List<Server> queryServiceListByType(Dao<Server, Integer> dao,String type) throws SQLException{
        QueryBuilder<Server, Integer> queryBuilder = dao.queryBuilder();
        queryBuilder.where().eq("Type", type);
        return queryBuilder.query();
    }
	
	public Server queryServiceByTypeAndUrl(Dao<Server, Integer> dao,String type,String url) throws SQLException{
	    QueryBuilder<Server, Integer> queryBuilder = dao.queryBuilder();
        queryBuilder.where().eq("Type", type);
        queryBuilder.where().eq("Url", url);
        return queryBuilder.queryForFirst();
	}
}
