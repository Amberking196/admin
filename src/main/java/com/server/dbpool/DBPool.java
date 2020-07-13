package com.server.dbpool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource; 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;  
 
/**
 * 数据库连结池
 ********************************************************************
 * 说明：<p>
 *      所有的连结都在dbManager.properties里修改
 ******************************************************************** 
 */ 
public class DBPool extends DataSourceUtils{

	public static Logger log = LogManager.getLogger( DBPool.class);     
	private static DBPool dbpoll=null;
	
	private static BasicDataSource datasource=null;
	private Properties config=null;
	
	private DBPool(){
		log.info("<DBPool>--<DBPool>--start");
		PropManager prop = new PropManager("dbManager.properties");
		config = prop.getProp();
		//datasource.setPoolPreparedStatements(true);
		datasource =new BasicDataSource();
		datasource.setDriverClassName(config.getProperty("DB.DriverClassName").trim());
		datasource.setUsername(config.getProperty("DB.Username").trim());
		datasource.setPassword(config.getProperty("DB.Password").trim());
		datasource.setUrl(config.getProperty("DB.ConnectionUrl").trim());
		datasource.setMinIdle(Integer.parseInt(config.getProperty("DB.MinIdle").trim()));
		datasource.setMaxIdle(Integer.parseInt(config.getProperty("DB.MaxIdle").trim()));
		datasource.setInitialSize(Integer.parseInt(config.getProperty("DB.InitialSize").trim()));
		datasource.setMaxWaitMillis(Integer.parseInt(config.getProperty("DB.MaxWait").trim()));
		datasource.setRemoveAbandonedTimeout(30);
		datasource.setRemoveAbandonedOnMaintenance(true);
		datasource.setRemoveAbandonedOnBorrow(true);
		datasource.setLogAbandoned(true);
		log.info("<DBPool>--<DBPool>--end");
	}
	
	public static DBPool getInstance(){
 
		if(dbpoll==null){
			synchronized (DBPool.class) {
				if(dbpoll==null){
					dbpoll=new DBPool();
				}
			}
		} 
		return dbpoll;
	}
	
	private Connection _getconn() throws SQLException{
		return super.getConnection(datasource);
	}
	public static Connection getConnection() throws SQLException{
		return getInstance()._getconn();
	}
	private void _show(){
 	}
	
	public static void show(){
		getInstance()._show();
	}
	
	private void _close() throws SQLException{
		datasource.close();
	}
	public static void close() throws SQLException{
		getInstance()._close();
	}

	public BasicDataSource getDatasource() {
		return datasource;
	}


	
	
}
