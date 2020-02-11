package com.server.module.system.memberManage.memberTypeManage;

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
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.Date;
import java.util.List;

/**
 * author name: why create time: 2018-09-20 16:08:54
 */
@Repository
public class MemberTypeDaoImpl extends BaseDao<MemberTypeBean> implements MemberTypeDao {

	private static Logger log = LogManager.getLogger(MemberTypeDaoImpl.class);

	/**
	 * 会员类型列表查询
	 */
	public ReturnDataUtil listPage(MemberTypeForm memberTypeForm) {
		log.info("<MemberTypeDaoImpl>-----<listPage>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,type,discount,money,validity,remark,createTime,createUser,updateTime,updateUser,deleteFlag from member_type ");
		sql.append(" where deleteFlag=0 ");
		if (memberTypeForm.getTypeId() != null) {
			sql.append(" and id = " + memberTypeForm.getTypeId() + " ");
		}
		sql.append(" order by createTime desc "); 
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("会员类型列表查询 SQL语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (memberTypeForm.getCurrentPage() - 1) * memberTypeForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + memberTypeForm.getPageSize());
			rs = pst.executeQuery();
			List<MemberTypeBean> list = Lists.newArrayList();
			while (rs.next()) {
				MemberTypeBean bean = new MemberTypeBean();
				bean.setId(rs.getLong("id"));
				bean.setType(rs.getString("type"));
				bean.setDiscount(rs.getDouble("discount"));
				bean.setMoney(rs.getDouble("money"));
				bean.setValidity(rs.getInt("validity"));
				bean.setRemark(rs.getString("remark"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(memberTypeForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<MemberTypeDaoImpl>-----<listPage>-----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<MemberTypeDaoImpl>-----<listPage>-----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 查询某一个会员类型信息
	 */
	public MemberTypeBean get(Object id) {
		log.info("<MemberTypeDaoImpl>-----<get>-----start");
		MemberTypeBean memberTypeBean = super.get(id);
		log.info("<MemberTypeDaoImpl>-----<get>-----end");
		return memberTypeBean;
	}

	/**
	 * 会员类型删除
	 */
	public boolean delete(Object id) {
		log.info("<MemberTypeDaoImpl>----<delete>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  member_type set deleteFlag= 1 where id in (" + id + ") ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("会员类型删除 sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				log.info("<MemberTypeDaoImpl>-----<delete>-----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return false;
	}

	/**
	 * 会员类型修改
	 */
	public boolean update(MemberTypeBean entity) {
		log.info("<MemberTypeDaoImpl>-----<update>-----start");
		boolean update = super.update(entity);
		log.info("<MemberTypeDaoImpl>-----<update>-----end");
		return update;
	}

	/**
	 * 会员类型增加
	 */
	public MemberTypeBean insert(MemberTypeBean entity) {
		log.info("<MemberTypeDaoImpl>-----<insert>-----start");
		MemberTypeBean insert = super.insert(entity);
		log.info("<MemberTypeDaoImpl>-----<insert>-----end");
		return insert;
	}

	/**
	 * 会员类型下拉列表
	 */
	public List<MemberTypeBean> list() {
		log.info("<MemberTypeDaoImpl>-----<list>-----start");
		StringBuilder sql = new StringBuilder();
		List<MemberTypeBean> list = Lists.newArrayList();
		sql.append(
				"select id,type,discount,money,validity,remark,createTime,createUser,updateTime,updateUser,deleteFlag from member_type ");
		sql.append(" where deleteFlag=0 ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("会员类型下拉列表 sql语句" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				MemberTypeBean bean = new MemberTypeBean();
				bean.setId(rs.getLong("id"));
				bean.setId(rs.getLong("id"));
				bean.setType(rs.getString("type"));
				bean.setDiscount(rs.getDouble("discount"));
				bean.setMoney(rs.getDouble("money"));
				bean.setRemark(rs.getString("remark"));
				bean.setValidity(rs.getInt("validity"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<MemberTypeDaoImpl>-----<list>-----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<MemberTypeDaoImpl>-----<list>-----end");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 校验会员类型不能重复
	 */
	@Override
	public boolean checkType(String type) {
		log.info("<MemberTypeDaoImpl>-----<checkType>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,type,discount,money,createTime,createUser,updateTime,updateUser,deleteFlag from member_type ");
		sql.append(" where deleteFlag=0 and type='" + type + "' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("校验会员类型不能重复 sql 语句" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				log.info("<MemberTypeDaoImpl>-----<checkType>-----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<MemberTypeDaoImpl>-----<checkType>-----end");
			return false;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return false;
	}
}
