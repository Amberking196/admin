package com.server.dbpool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;  

import com.server.dbpool.DBPool;

/**
 * 数据库的连结接口 *******************************************************************
 * 说明：
 * <p> 
 */
public class BaseDB   { 

	public static Logger log = LogManager.getLogger( BaseDB.class);    
	public static long count = 0;
	private static Object lock = new Object();
	private static int counter =0;
	private int i=1;
	private static PropManager prop = new PropManager("dbManager.properties");
	private static Properties config = prop.getProp();
	private static int limit = Integer.parseInt(config.getProperty("DB.MaxActive").trim());
	
	public static Connection openConnection() {
		Connection result=null;
		log.info("创建数据库连接，最大连接数为："+limit+",当前数据库连接数="+counter);
		synchronized (lock) {
			while (counter >= limit) {
				log.info("当前数据库连接超过最大连接数，请等待连接释放！！！");
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			counter++;
			 	try {
				result = DBPool.getConnection(); 
			} catch (SQLException e) { 
				e.printStackTrace();
			} 
		} 
		return result;
	}

	public void closeConnection(ResultSet rs, PreparedStatement ps,
			Connection conn) { 
		//log.info("最大连接数为："+limit+",当前数据库连接数="+counter);  
		if (rs != null) {
			try {
				//log.info("关闭游标RS");  
				rs.close();
			} catch (SQLException e) { 
				e.printStackTrace();
			}
		}
		if (ps != null) {
			try {
				//log.info("关闭游标PS");  
				ps.close();
			} catch (SQLException e) { 
				e.printStackTrace();
			}
		}
		if (conn != null) {
//			conn.close();
			DBPool.releaseConnection(conn, DBPool.getInstance().getDatasource());
			counter--; 
			log.info("关闭数据库连接-----当前连接数："+counter);  
			synchronized (lock) {
				lock.notifyAll();
			}
		}
 
	}

	public static void closeConnection(Connection conn)   {
		try {
			if (conn != null) {
//				conn.close();
				DBPool.releaseConnection(conn, DBPool.getInstance().getDatasource());
				synchronized (lock) { 
					lock.notifyAll();
				}
			}
		}finally {
			counter--;  
			log.info("关闭数据库连接，最大连接数为："+limit+",当前数据库连接数="+counter);   
		} 
 
	}
	@Override
	public int hashCode() { 
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + i; 
		return result;
	}

	@Override
	public boolean equals(Object obj) { 
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BaseDB other = (BaseDB) obj;
		if (i != other.i)
			return false;
		return true;
	}

}

