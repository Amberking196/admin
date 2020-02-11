package com.server.module.customer.complain.replyCommonLanguage;

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

/**
 * author name: why create time: 2019-01-04 09:50:27
 */
@Repository
public class ComplainReplyCommonLanguageDaoImpl extends BaseDao<ComplainReplyCommonLanguageBean>
		implements ComplainReplyCommonLanguageDao {

	private static Logger log = LogManager.getLogger(ComplainReplyCommonLanguageDaoImpl.class);

	public ReturnDataUtil listPage(ComplainReplyCommonLanguageForm form) {
		log.info("<ComplainReplyCommonLanguageDaoImpl>-----<listPage>-----<start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select id,commonLanguage,createTime,createUser,updateTime,updateUser,deleteFlag from complain_reply_common_language ");
		sql.append(" where 1=1 and deleteFlag=0 order by createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Integer num=0;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if(form.getIsShowAll()==1) {
				pst = conn.prepareStatement(sql.toString());
			}else {
				long off = (form.getCurrentPage() - 1) * form.getPageSize();
				pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + form.getPageSize());
			}
			rs = pst.executeQuery();
			List<ComplainReplyCommonLanguageBean> list = Lists.newArrayList();
			while (rs.next()) {
				num++;
				ComplainReplyCommonLanguageBean bean = new ComplainReplyCommonLanguageBean();
				bean.setNum(num);
				bean.setId(rs.getLong("id"));
				bean.setCommonLanguage(rs.getString("commonLanguage"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			if (showSql) {
				log.info("查询常用语SQL语句："+sql);
			}
			data.setCurrentPage(form.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ComplainReplyCommonLanguageDaoImpl>-----<listPage>-----<end>");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ComplainReplyCommonLanguageDaoImpl>-----<listPage>-----<end>");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public ComplainReplyCommonLanguageBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		log.info("<ComplainReplyCommonLanguageDaoImpl>-----<listPage>-----<start>");
		boolean flag=false;
		StringBuilder sql = new StringBuilder();
		sql.append(" update complain_reply_common_language set deleteFlag=1 where id='"+id+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("删除常用语sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate>0) {
				flag=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<ComplainReplyCommonLanguageDaoImpl>-----<listPage>-----<end>");
		return flag;
	}

	public boolean update(ComplainReplyCommonLanguageBean entity) {
		log.info("<ComplainReplyCommonLanguageDaoImpl>-----<update>-----<start>");
		boolean update = super.update(entity);
		log.info("<ComplainReplyCommonLanguageDaoImpl>-----<update>-----<end>");
		return update;
	}

	public ComplainReplyCommonLanguageBean insert(ComplainReplyCommonLanguageBean entity) {
		log.info("<ComplainReplyCommonLanguageDaoImpl>------<insert>-----start");
		ComplainReplyCommonLanguageBean bean = super.insert(entity);
		log.info("<ComplainReplyCommonLanguageDaoImpl>------<insert>-----end");
		return bean;
	}

	public List<ComplainReplyCommonLanguageBean> list(ComplainReplyCommonLanguageForm form) {
		return null;
	}
}
