package com.server.module.customer.product.shoppingGoodsSpellGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-10-16 16:41:04
 */
@Repository
public class ShoppingGoodsSpellGroupDaoImpl extends BaseDao<ShoppingGoodsSpellGroupBean>
		implements ShoppingGoodsSpellGroupDao {

	private static Logger log = LogManager.getLogger(ShoppingGoodsSpellGroupDaoImpl.class);

	/**
	 * 商品团购列表
	 */
	@SuppressWarnings("resource")
	public ReturnDataUtil listPage(ShoppingGoodsSpellGroupForm shoppingGoodsSpellGroupForm) {
		log.info("<ShoppingGoodsSpellGroupDaoImpl>------<listPage>-------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,goodsId,spellGroupPrice,minimumGroupSize,timeLimit,startTime,endTime,createTime,createUser,updateTime,updateUser,deleteFlag,vmCode,numberLimit,successLimit,userType,allowRefund,theme ");
		sql.append(" from shopping_goods_spellGroup where 1=1 and deleteFlag = 0 ");
		if(shoppingGoodsSpellGroupForm.getGoodsId()!=null) {
			sql.append(" and goodsId='"+shoppingGoodsSpellGroupForm.getGoodsId()+"' ");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("商品团购列表sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (shoppingGoodsSpellGroupForm.getCurrentPage() - 1) * shoppingGoodsSpellGroupForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + shoppingGoodsSpellGroupForm.getPageSize());
			rs = pst.executeQuery();
			List<ShoppingGoodsSpellGroupBean> list = Lists.newArrayList();
			while (rs.next()) {
				ShoppingGoodsSpellGroupBean bean = new ShoppingGoodsSpellGroupBean();
				bean.setId(rs.getLong("id"));
				bean.setGoodsId(rs.getLong("goodsId"));
				bean.setSpellGroupPrice(rs.getBigDecimal("spellGroupPrice"));
				bean.setMinimumGroupSize(rs.getLong("minimumGroupSize"));
				bean.setTimeLimit(rs.getInt("timeLimit"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setStartTimeLabel(rs.getDate("startTime"));
				bean.setEndTimeLabel(rs.getDate("endTime"));
				
				bean.setVmCode(rs.getString("vmCode"));
				bean.setNumberLimit(rs.getInt("numberLimit"));
				bean.setSuccessLimit(rs.getInt("successLimit"));
				bean.setUserType(rs.getInt("userType"));
				bean.setAllowRefund(rs.getInt("allowRefund"));
				bean.setTheme(rs.getString("theme"));
				
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(shoppingGoodsSpellGroupForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ShoppingGoodsSpellGroupDaoImpl>------<listPage>-------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsSpellGroupDaoImpl>------<listPage>-------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public ShoppingGoodsSpellGroupBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		log.info("<ShoppingGoodsSpellGroupDaoImpl>------<delete>-------end");
		StringBuilder sql = new StringBuilder();
		sql.append(" update shopping_goods_spellGroup set deleteFlag = 1");
		sql.append(" where  id in (" + id + ") ");
		Connection conn = null;
		PreparedStatement pst = null;
		int rs = 0;
		log.info("判断是否是拼团商品sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeUpdate();
			while (rs>0) {
				return true;
			}
			log.info("<ShoppingGoodsSpellGroupDaoImpl>------<delete>-------end");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsSpellGroupDaoImpl>------<delete>-------end");
			return false;
		} finally {
			this.closeConnection(null, pst, conn);
		}
		return false;
	}

	public boolean update(ShoppingGoodsSpellGroupBean entity) {
		return super.update(entity);
	}

	/**
	 * 商品团购设置
	 */
	public ShoppingGoodsSpellGroupBean insert(ShoppingGoodsSpellGroupBean entity) {
		log.info("<ShoppingGoodsSpellGroupDaoImpl>------<insert>------start");
		ShoppingGoodsSpellGroupBean bean = super.insert(entity);
		log.info("<ShoppingGoodsSpellGroupDaoImpl>------<insert>------end");
		return bean;
	}

	public List<ShoppingGoodsSpellGroupBean> list(ShoppingGoodsSpellGroupForm condition) {
		return null;
	}

	/**
	 * 判断是否是拼团商品
	 */
	@Override
	public ShoppingGoodsSpellGroupBean isConglomerateCommodity(Long shoppingGoodsId) {
		log.info("<ShoppingGoodsSpellGroupDaoImpl>------<isConglomerateCommodity>-------start");
        ShoppingGoodsSpellGroupBean bean = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select id,goodsId,spellGroupPrice,minimumGroupSize,startTime,endTime,createTime,createUser,updateTime,updateUser,deleteFlag, ");
		sql.append(" vmCode,numberLimit,successLimit,userType,allowRefund,theme ");
		sql.append(" from shopping_goods_spellGroup where  goodsId='"+shoppingGoodsId+"' and endTime>=now() ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("判断是否是拼团商品sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next() && rs!=null) {
				bean = new ShoppingGoodsSpellGroupBean();
				bean.setId(rs.getLong("id"));
				bean.setGoodsId(rs.getLong("goodsId"));
				bean.setSpellGroupPrice(rs.getBigDecimal("spellGroupPrice"));
				bean.setMinimumGroupSize(rs.getLong("minimumGroupSize"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setNumberLimit(rs.getInt("numberLimit"));
				bean.setSuccessLimit(rs.getInt("successLimit"));
				bean.setUserType(rs.getInt("userType"));
				bean.setTheme(rs.getString("theme"));
			}
			log.info("<ShoppingGoodsSpellGroupDaoImpl>------<isConglomerateCommodity>-------end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsSpellGroupDaoImpl>------<isConglomerateCommodity>-------end");
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
}
