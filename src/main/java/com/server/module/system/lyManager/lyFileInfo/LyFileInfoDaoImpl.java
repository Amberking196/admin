package com.server.module.system.lyManager.lyFileInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * 
 * author name: why
 * create time: 2018-04-04 10:00:39
 */
@Repository
public class LyFileInfoDaoImpl extends BaseDao<LyFileInfoBean> implements LyFileInfoDao {


	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class); 	 
	
	/**
	 * 查询 文件管理列表
	 */
	@SuppressWarnings("resource")
	@Override
	public ReturnDataUtil listPage(LyFileInfoCondition condition) {
		 ReturnDataUtil data=new ReturnDataUtil();
	     StringBuilder sql=new StringBuilder();
	      sql.append("select name,type,path,remark,size,version,createTime,updateTime from ly_file_info where 1=1");
			List<Object> plist=Lists.newArrayList();
		
			if (StringUtil.isNotBlank(condition.getName())){
				sql.append(" and name = ?");
				plist.add(condition.getName());
			}
			if (StringUtil.isNotBlank(condition.getVersion())){
				sql.append(" and version = ?");
				plist.add(condition.getVersion());
			}
			Connection conn=null;
			PreparedStatement pst=null;
			ResultSet rs=null;

			try {
				conn=openConnection();
				pst=conn.prepareStatement(super.countSql(sql.toString()));
				if (plist!=null && plist.size()>0)
				for (int i=0;i<plist.size();i++){
					pst.setObject(i+1, plist.get(i));
				}
				rs = pst.executeQuery();
				long count=0;
				while(rs.next()){
					count=rs.getInt(1);
				}
				long off=(condition.getCurrentPage()-1)*condition.getPageSize();			
				pst=conn.prepareStatement(sql.toString()+" limit "+off+","+condition.getPageSize());
				if (plist!=null && plist.size()>0)
				for (int i=0;i<plist.size();i++){
					pst.setObject(i+1, plist.get(i));
				}
				rs = pst.executeQuery();
				List<LyFileInfoBean> list=Lists.newArrayList();
				while(rs.next()){
					LyFileInfoBean bean = new LyFileInfoBean();
					bean.setName(rs.getString(1));
					bean.setType(rs.getString(2));
					bean.setPath(rs.getString(3));
					bean.setRemark(rs.getString(4));
					bean.setSize(rs.getInt(5));
					bean.setVersion(rs.getString(6));
					bean.setCreateTime(rs.getDate(7));
					bean.setUpdateTime(rs.getDate(8));
					list.add(bean);
				}
				if (showSql){
					log.info(sql);
					log.info(plist.toString());
				}			
				data.setCurrentPage(condition.getCurrentPage());
				data.setTotal(count);
				data.setReturnObject(list);
				data.setStatus(1);
				return data;
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return data;
			} finally{
				try {
					rs.close();
					pst.close();
					closeConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	}

	/**
	 * 编辑 文件
	 */
	@Override
	public boolean updateEntity(LyFileInfoBean entity) {
		// TODO Auto-generated method stub
		return super.update(entity);
	}
	
	/**
	 * 删除文件
	 */
	public boolean del(LyFileInfoBean name) {
		return super.del(name);
		
	}
}
