package com.server.module.system.forumManage.forumFilterManage;

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
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * 
 * @author why
 * @date: 2019年3月14日 上午11:40:31
 */
@Repository
public class ForumFilterDaoImpl extends BaseDao<ForumFilterBean> implements ForumFilterDao {

	private static Logger log = LogManager.getLogger(ForumFilterDaoImpl.class);

	public ReturnDataUtil listPage(ForumFilterForm forumFilterForm) {
		log.info("<ForumFilterDaoImpl>-------<listPage>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select id,noun,createTime,createUser,updateTime,updateUser,deleteFlag from  ");
		sql.append(" forum_filter where deleteFlag=0   ");
		if(StringUtil.isNotBlank(forumFilterForm.getNoun())) {
			sql.append(" and noun like '%"+forumFilterForm.getNoun().trim()+"%' ");
		}
		sql.append(" order by createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("敏感名词列表查询sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (forumFilterForm.getCurrentPage() - 1) * forumFilterForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + forumFilterForm.getPageSize());
			rs = pst.executeQuery();
			List<ForumFilterBean> list = Lists.newArrayList();
			while (rs.next()) {
				ForumFilterBean bean = new ForumFilterBean();
				bean.setId(rs.getLong("id"));
				bean.setNoun(rs.getString("noun"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			data.setCurrentPage(forumFilterForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ForumFilterDaoImpl>-------<listPage>-----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ForumFilterDaoImpl>-------<listPage>-----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public ForumFilterBean get(Object id) {
		log.info("<ForumFilterDaoImpl>---------<get>------start");
		ForumFilterBean forumFilterBean = super.get(id);
		log.info("<ForumFilterDaoImpl>---------<get>------end");
		return forumFilterBean;
	}

	public boolean delete(Object id) {
		log.info("<ForumFilterDaoImpl>------<delete>-----start");
		StringBuffer sql = new StringBuffer();
		sql.append(" update forum_filter set deleteFlag=1 where id=" + id);
		Connection conn = null;
		PreparedStatement pst = null;
		boolean flag = false;
		log.info("删除过滤名词sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, pst, conn);
		}
		log.info("<ForumFilterDaoImpl>------<delete>-----end");
		return flag;
	}

	public boolean update(ForumFilterBean entity) {
		log.info("<ForumFilterDaoImpl>------<update>-----start");
		boolean update = super.update(entity);
		log.info("<ForumFilterDaoImpl>------<update>-----end");
		return update;
	}

	public ForumFilterBean insert(ForumFilterBean entity) {
		log.info("<ForumFilterDaoImpl>-------<insert>-----start");
		ForumFilterBean bean = super.insert(entity);
		log.info("<ForumFilterDaoImpl>-------<insert>-----end");
		return bean;
	}

	@Override
	public List<ForumFilterBean> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNounExists(String noun) {
		log.info("<ForumFilterDaoImpl>--------<isNounExists>-------start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,noun  from forum_filter where noun='" + noun + "'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag = true;
		log.info("判断关键字是否存在sql:"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			if (rs.next() && rs != null) {
				flag=false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, pst, conn);
		}

		log.info("<ForumFilterDaoImpl>--------<isNounExists>-------end");
		return flag;
	}
}
