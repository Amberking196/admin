package com.server.module.system.officialManage.officialMessage;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;
/**
 * author name: hjc
 * create time: 2018-08-02 10:24:08
 */ 
@Repository
public class  OfficialMessageDaoImpl extends BaseDao<OfficialMessageBean> implements OfficialMessageDao{

	public static Logger log = LogManager.getLogger(OfficialMessageDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;
	/**
	 * 官网留言列表
	 * @param officialMessageForm 查询条件对象
	 * @return
	 */
	public ReturnDataUtil listPage(OfficialMessageForm officialMessageForm) {
		log.info("<OfficialMessageDaoImpl>----<listPage>----start");
		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		sql.append("select id,name,mail,phone,content,createTime,updateTime,createUser,updateUser,deleteFlag from official_message where 1=1 ");
		if(StringUtils.isNotBlank(officialMessageForm.getName())) {
			sql.append(" and name like '%"+officialMessageForm.getName()+"%'");
		}
		if(StringUtils.isNotBlank(officialMessageForm.getPhone())) {
			sql.append(" and phone like '%"+officialMessageForm.getPhone()+"%'");
		}
		if(StringUtils.isNotBlank(officialMessageForm.getMail())) {
			sql.append(" and mail like '%"+officialMessageForm.getMail()+"%'");
		}
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		log.info("列表查询sql语句：》》" + sql.toString());
		try {
			conn=openConnection();
			pst=conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count=0;
			while(rs.next()){
				count=rs.getInt(1);
			}
			long off=(officialMessageForm.getCurrentPage()-1)*officialMessageForm.getPageSize();
			pst=conn.prepareStatement(sql.toString()+" limit "+off+","+officialMessageForm.getPageSize());
			rs = pst.executeQuery();
			List<OfficialMessageBean> list=Lists.newArrayList();
			while(rs.next()){
				OfficialMessageBean bean = new OfficialMessageBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setMail(rs.getString("mail"));
				bean.setPhone(rs.getString("phone"));
				bean.setContent(rs.getString("content"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			if (showSql){
				log.info(sql);
			}
			data.setCurrentPage(officialMessageForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<OfficialMessageDaoImpl>----<listPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally{
			this.closeConnection(rs, pst, conn);
		}
	}
	/**
	 * 官网留言获取方法
	 * @param id 官网留言id
	 * @return
	 */
	public OfficialMessageBean get(Object id) {
		return super.get(id);
	}
	/**
	 * 官网留言删除方法
	 * @param id 官网留言id
	 * @return
	 */
	public boolean delete(Object id) {
		OfficialMessageBean entity=new OfficialMessageBean();
		return super.del(entity);
	}
	/**
	 * 官网留言修改方法
	 * @param entity 官网留言值对象
	 * @return
	 */
	public boolean update(OfficialMessageBean entity) {
		return super.update(entity);
	}
	/**
	 * 官网留言添加方法
	 * @param entity 官网留言值对象
	 * @return
	 */
	public OfficialMessageBean insert(OfficialMessageBean entity) {
		return super.insert(entity);
	}
}

