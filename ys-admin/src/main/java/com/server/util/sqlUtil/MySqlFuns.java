package com.server.util.sqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.server.dbpool.BaseDB;
 

public class MySqlFuns extends BaseDB{

	public static Logger log = LogManager.getLogger( MySqlFuns.class); 
	public static final String SQL_GET_ID = "SELECT LAST_INSERT_ID() AS id";
	public final static String TOTAL_SQL = " SELECT FOUND_ROWS() AS total";
	
	public int insert(String sql){
		log.info("<MySqlFuns>--<insert>--sql:" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int re=0;
		boolean isTransaction = false;
		try {
			conn = openConnection();
			boolean isAutoCommit = conn.getAutoCommit();
			if(isAutoCommit){
				conn.setAutoCommit(false);
			}else{
				isTransaction = true;
			}
			ps = conn.prepareStatement(sql);
			re=ps.executeUpdate();  
			if(!isTransaction){
				conn.commit();
			}
		}  catch (SQLException e) {   
			try {
				if(isTransaction){
					throw new RuntimeException();
				}else{
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return re;
	}
	
	public int insert(String sql,List<Object> param){
		
		Connection conn = null;
		PreparedStatement ps = null;
		int re=0;
		boolean isTransaction = false;
		try {
			conn = openConnection();
			boolean isAutoCommit = conn.getAutoCommit();
			if(isAutoCommit){
				conn.setAutoCommit(false);
			}else{
				isTransaction = true;
			}
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			String logSql = sql;
			for (int i = 0; i < param.size(); i++) {
				ps.setObject(i+1,param.get(i));
				if(param.get(i)!=null){
					if(param.get(i).getClass().getName().equals("java.lang.String")){
						logSql = logSql.replaceFirst("[\\?]", "'"+param.get(i).toString()+"'");
					}else{
						logSql = logSql.replaceFirst("[\\?]", param.get(i).toString());
					}
				}else{
					logSql = logSql.replaceFirst("[\\?]","null");
				}
				
			}
			log.info("sql语句："+logSql);
			re=ps.executeUpdate();  
			if(!isTransaction){
				conn.commit();
			}
		}  catch (SQLException e) {   
			try {
				if(isTransaction){
					throw new RuntimeException();
				}else{
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return re;
	}
	 
	public int getId(Connection conn,PreparedStatement ps){
		ResultSet rs  = null;
		try {
			ps = conn.prepareStatement(SQL_GET_ID);
			rs  = ps.executeQuery();
			if(rs.next()){
				return rs.getInt("id");
			}
		} catch (SQLException e) {
			  e.printStackTrace(); 
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		return 0;
		
	}  

	public int insertGetID(String sql){
		log.info("<MySqlFuns>--<insertGetID>--sql:" + sql);
		int id = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		boolean isTransaction = false;
		try {
			conn = openConnection();
			boolean isAutoCommit = conn.getAutoCommit();
			if(isAutoCommit){
				conn.setAutoCommit(false);
			}else{
				isTransaction = true;
			}
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			id = getId(conn,ps);
			if(!isTransaction){
				conn.commit();
			}
		}  catch (SQLException e) { 
			try {
				if(isTransaction){
					throw new RuntimeException();
				}else{
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}
	
	 
	public int delete(String sql){
		log.info("<MySqlFuns>--<delete>--sql:" + sql);
		int result = 0;
		boolean isTransaction = false;
		Connection conn = null;
			PreparedStatement ps = null;
			try {
				conn = openConnection();
				boolean isAutoCommit = conn.getAutoCommit();
				if(isAutoCommit){
					conn.setAutoCommit(false);
				}else{
					isTransaction = true;
				}
				ps = conn.prepareStatement(sql);
				result = ps.executeUpdate(); 
				if(!isTransaction){
					conn.commit();
				}
			}  catch (SQLException e) {  
				try {
					if(isTransaction){
						throw new RuntimeException();
					}else{
						conn.rollback();
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} finally {
				this.closeConnection(null, ps, conn);
			}
			return result;
	} 
	
	public int upate(String sql) {

		log.info("<MySqlFuns>--<upate>--sql:" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int re=0;
		boolean isTransaction = false;
		try {
			conn = openConnection();
			boolean isAutoCommit = conn.getAutoCommit();
			if(isAutoCommit){
				conn.setAutoCommit(false);
			}else{
				isTransaction = true;
			}
			ps = conn.prepareStatement(sql);
			re=ps.executeUpdate(); 
			if(!isTransaction){
				conn.commit();
			}
		}  catch (SQLException e) { 
			e.printStackTrace();
			try {
				if(isTransaction){
					throw new RuntimeException();
				}else{
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} 
		} finally {
			this.closeConnection(null, ps, conn);
		}
		return re;
	}
	
	public int upate(String sql,List<Object> param) {
		Connection conn = null;
		PreparedStatement ps = null;
		int re=0;
		boolean isTransaction = false;
		try {
			conn = openConnection();
			boolean isAutoCommit = conn.getAutoCommit();
			if(isAutoCommit){
				conn.setAutoCommit(false);
			}else{
				isTransaction = true;
			}
			ps = conn.prepareStatement(sql);
			String logSql = sql;
			for (int i = 0; i < param.size(); i++) {
				ps.setObject(i+1, param.get(i));
				if(param.get(i)!=null){
					if(param.get(i).getClass().getName().equals("java.lang.String")){
						logSql = logSql.replaceFirst("[\\?]", "'"+param.get(i).toString()+"'");
					}else{
						logSql = logSql.replaceFirst("[\\?]", param.get(i).toString());
					}
				}else{
					logSql = logSql.replaceFirst("[\\?]", "null");
				}
			}
			log.info("sql语句："+logSql);
			re=ps.executeUpdate(); 
			if(!isTransaction){
				conn.commit();
			}
		}  catch (SQLException e) { 
			e.printStackTrace();
			try {
				if(isTransaction){
					throw new RuntimeException();
				}else{
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} 
		} finally {
			this.closeConnection(null, ps, conn);
		}
		return re;
	}
	
	public void query(String sql){
		log.info("<MySqlFuns>--<query>--sql:" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql);
            ResultSet rs =  ps.executeQuery();
            while(rs.next()){
            //	String name=rs.getString(1);
            	String companyId=rs.getString("companyId");
            	System.out.println(companyId);
            }
			//conn.commit();
		}  catch (SQLException e) { 
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public int insertGetID(String sql,List<Object> param){
		int id = -1;
		Connection conn = null;
		PreparedStatement ps = null;
		boolean isTransaction = false;
		try {
			conn = openConnection();
			boolean isAutoCommit = conn.getAutoCommit();
			if(isAutoCommit){
				conn.setAutoCommit(false);
			}else{
				isTransaction = true;
			}
			ps = conn.prepareStatement(sql);
			String logSql = sql;
			for (int i = 0; i < param.size(); i++) {
				ps.setObject(i+1,param.get(i));
				if(param.get(i)!=null){
					if(param.get(i).getClass().getName().equals("java.lang.String")){
						logSql = logSql.replaceFirst("[\\?]", "'"+param.get(i).toString()+"'");
					}else{
						logSql = logSql.replaceFirst("[\\?]", param.get(i).toString());
					}
				}else{
					logSql = logSql.replaceFirst("[\\?]","null");
				}
				
			}
			log.info("sql语句："+logSql);
			int result = ps.executeUpdate();    
			if(result == 1){
				id = getId(conn,ps);
			}
			if (!isTransaction) {
				conn.commit();
			}
		}  catch (SQLException e) { 
			e.printStackTrace();
			try {
				if (isTransaction) {
					throw new RuntimeException();
				}else{
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}
	/**
	 * 该方法仅仅适用于 sql查询中使用了SQL_CALC_FOUND_ROWS
	 * @author hebiting
	 * @date 2019年1月21日上午10:28:05
	 * @return
	 */
	public Long getTotal(){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long total = 0L;
		try{
			conn = openConnection();
			ps = conn.prepareStatement(TOTAL_SQL);
			rs = ps.executeQuery();
			while(rs != null && rs.next()){
				total = rs.getLong("total");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeConnection(rs, ps, conn);
		}
		return total;
	}
}
