package com.server.module.customer.coupon;

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
import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-07-10 19:12:50
 */
@Repository
public class CouponProductDao1Impl extends BaseDao<CouponProduct1Bean> implements CouponProduct1Dao {

	private static Logger log = LogManager.getLogger(CouponProductDao1Impl.class);
	
	

	public ReturnDataUtil listPage(CouponProductCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,couponId,productId,createTime,createUser,updateTime,updateUser,deleteFlag from coupon_product where 1=1 ");
		List<Object> plist = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<CouponProduct1Bean> list = Lists.newArrayList();
			while (rs.next()) {
				CouponProduct1Bean bean = new CouponProduct1Bean();
				bean.setId(rs.getLong("id"));
				bean.setCouponId(rs.getLong("couponId"));
				bean.setProductId(rs.getLong("productId"));
				// bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				// bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			if (showSql) {
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
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<CouponProduct1Bean> list(int couponId) {
		String sql = "select id,couponId,productId,createTime,createUser,updateTime,updateUser,deleteFlag from coupon_product where couponId="
				+ couponId;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<CouponProduct1Bean> list = Lists.newArrayList();

		try {
			conn = openConnection();

			pst = conn.prepareStatement(sql);

			rs = pst.executeQuery();
			while (rs.next()) {
				CouponProduct1Bean bean = new CouponProduct1Bean();
				bean.setId(rs.getLong("id"));
				bean.setCouponId(rs.getLong("couponId"));
				bean.setProductId(rs.getLong("productId"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public CouponProduct1Bean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		CouponProduct1Bean entity = new CouponProduct1Bean();
		return super.del(entity);
	}

	public boolean update(CouponProduct1Bean entity) {
		return super.update(entity);
	}

	public CouponProduct1Bean insert(CouponProduct1Bean entity) {
		return super.insert(entity);
	}

	

	/**
	 * 查询优惠券下绑定的商品
	 */
	@Override
	public List<ShoppingGoodsBean> findShoppingGoodsBean(int couponId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select  sg.id,sg.basicItemId ,sg.`name`,sg.pic,sg.salesPrice  from coupon_product  cp ");
		sql.append(" inner join  shopping_goods sg on cp.productId=sg.id where  couponId='"+couponId+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询优惠券下绑定的商品 sql-语句"+sql.toString());
		List<ShoppingGoodsBean> list = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next() && rs!=null) {
				ShoppingGoodsBean bean = new ShoppingGoodsBean();
				bean.setId(rs.getLong("id"));
				bean.setBasicItemId(rs.getLong("basicItemId"));
				bean.setName(rs.getString("name"));
				bean.setPic(rs.getString("pic"));
				bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
}
