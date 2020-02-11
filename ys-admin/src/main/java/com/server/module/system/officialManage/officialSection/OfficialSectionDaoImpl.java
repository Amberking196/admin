package com.server.module.system.officialManage.officialSection;

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
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;
/**
 * author name: hjc
 * create time: 2018-08-01 10:02:19
 */ 
@Repository
public class  OfficialSectionDaoImpl extends BaseDao<OfficialSectionBean> implements OfficialSectionDao{

	public static Logger log = LogManager.getLogger(OfficialSectionDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;
	/**
	 * 官网栏目列表
	 * @param officialSectionForm
	 * @return
	 */
	public List<OfficialSectionBean> listPage(OfficialSectionForm officialSectionForm) {
		log.info("<OfficialSectionDaoImpl>----<listPage>----start");
		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		sql.append(" select id,pid,name,url,createTime,updateTime,createUser,updateUser,deleteFlag from official_section where 1=1 and deleteFlag = 0 ");
		if(officialSectionForm.getThisId()!=null) {
			sql.append("  and id!="+officialSectionForm.getThisId());
		}
		if(officialSectionForm.getSonId()!=null) {
			sql.append("  and pid="+officialSectionForm.getSonId());
		}
		if(officialSectionForm.getId()!=null) {
			sql.append("  and id="+officialSectionForm.getId());
		}
		if(officialSectionForm.getPid()!=null) {
			sql.append("  and pid="+officialSectionForm.getPid());
		}
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<OfficialSectionBean> list=Lists.newArrayList();
		log.info("列表查询sql语句：》》" + sql.toString());
		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while(rs.next()){
				OfficialSectionBean bean = new OfficialSectionBean();
				bean.setId(rs.getLong("id"));
				bean.setPid(rs.getLong("pid"));
				bean.setName(rs.getString("name"));
				bean.setUrl(rs.getString("url"));
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
			data.setCurrentPage(officialSectionForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<OfficialSectionDaoImpl>----<listPage>----end");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally{
			this.closeConnection(rs, pst, conn);
		}
		return list;
	}
	
	
	//查询本栏目与所有子栏目
	public List<OfficialSectionBean> findSonSectionList(OfficialSectionBean officialSectionBean) {
		log.info("<OfficialSectionDaoImpl>----<findSonSectionList>----start");
		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		sql.append("select id,pid,name,url,createTime,updateTime,createUser,updateUser,deleteFlag from official_section where 1=1 and deleteFlag = 0");
		sql.append(" and pid ="+officialSectionBean.getId());
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<OfficialSectionBean> list=Lists.newArrayList();
		list.add(officialSectionBean);
		log.info("列表查询sql语句：》》" + sql.toString());
		try {
			conn=openConnection();
			conn.setAutoCommit(false);
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while(rs.next()){
				OfficialSectionBean bean = new OfficialSectionBean();
				bean.setId(rs.getLong("id"));
				bean.setPid(rs.getLong("pid"));
				bean.setName(rs.getString("name"));
				bean.setUrl(rs.getString("url"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			StringBuffer sql2=new StringBuffer();
			sql2.append("select id from official_section WHERE deleteFlag=0 and pid = "+officialSectionBean.getId());
			while(rs != null) {
				StringBuffer sql3=new StringBuffer();
				sql3.append("select id,pid,name,url,createTime,updateTime,createUser,updateUser,deleteFlag from official_section  where deleteFlag=0 and pid in("+sql2+")");
				pst = conn.prepareStatement(sql3.toString());
				rs = pst.executeQuery();
				while(rs != null && rs.next()){
					OfficialSectionBean bean = new OfficialSectionBean();
					bean.setId(rs.getLong("id"));
					bean.setPid(rs.getLong("pid"));
					bean.setName(rs.getString("name"));
					bean.setUrl(rs.getString("url"));
					bean.setCreateTime(rs.getDate("createTime"));
					bean.setUpdateTime(rs.getDate("updateTime"));
					bean.setCreateUser(rs.getLong("createUser"));
					bean.setUpdateUser(rs.getLong("updateUser"));
					bean.setDeleteFlag(rs.getInt("deleteFlag"));
					list.add(bean);
				}
			    if(rs.first() == false) {
					break;
				}
			    //截取部分sql
				sql2=sql3.replace(7, 77, "id");
			}
			conn.commit();
			if (showSql){
				log.info(sql);
			}
			log.info("<OfficialSectionDaoImpl>----<listPage>----end");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally{
			this.closeConnection(rs, pst, conn);
		}
		return list;
	}

	/**
	 * 官网栏目获取
	 * @param id
	 * @return
	 */
	public OfficialSectionBean get(Object id) {
		return super.get(id);
	}
	/**
	 * 官网栏目删除
	 * @param id
	 * @return
	 */
	public boolean delete(Object id) {
		OfficialSectionBean entity=new OfficialSectionBean();
		return super.del(entity);
	}
	/**
	 * 官网栏目修改
	 * @param entity
	 * @return
	 */
	public boolean update(OfficialSectionBean entity) {
		return super.update(entity);
	}
	/**
	 * 官网栏目添加
	 * @param entity
	 * @return
	 */
	public OfficialSectionBean insert(OfficialSectionBean entity) {
		return super.insert(entity);
	}
}

