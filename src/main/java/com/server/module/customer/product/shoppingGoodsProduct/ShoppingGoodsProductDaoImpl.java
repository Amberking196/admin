package com.server.module.customer.product.shoppingGoodsProduct;

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
 * author name: why create time: 2018-09-14 09:53:47
 */
@Repository
public class ShoppingGoodsProductDaoImpl extends BaseDao<ShoppingGoodsProductBean> implements ShoppingGoodsProductDao {

	private static Logger log = LogManager.getLogger(ShoppingGoodsProductDaoImpl.class);

	/**
	 * 商城商品关联商品列表查询
	 */
	@SuppressWarnings("resource")
	public ReturnDataUtil listPage(ShoppingGoodsProductForm shoppingGoodsProductForm) {
		log.info("<ShoppingGoodsProductDaoImpl>----<listPage>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,goodsId,itemId,createTime,createUser,updateTime,updateUser,deleteFlag  ");
		sql.append(" from shopping_goods_product where 1=1 ");
		List<Object> plist = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("商城商品关联商品列表查询 sql语句：" + sql.toString());
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
			long off = (shoppingGoodsProductForm.getCurrentPage() - 1) * shoppingGoodsProductForm.getPageSize();
			pst = conn
					.prepareStatement(sql.toString() + " limit " + off + "," + shoppingGoodsProductForm.getPageSize());

			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<ShoppingGoodsProductBean> list = Lists.newArrayList();
			while (rs.next()) {
				ShoppingGoodsProductBean bean = new ShoppingGoodsProductBean();
				bean.setId(rs.getLong("id"));
				bean.setGoodsId(rs.getLong("goodsId"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			data.setCurrentPage(shoppingGoodsProductForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ShoppingGoodsProductDaoImpl>----<listPage>------start");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsProductDaoImpl>----<listPage>------start");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public ShoppingGoodsProductBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		ShoppingGoodsProductBean entity = new ShoppingGoodsProductBean();
		return super.del(entity);
	}

	public boolean update(ShoppingGoodsProductBean entity) {
		return super.update(entity);
	}

	/**
	 * 绑定商品
	 */
	public ShoppingGoodsProductBean insert(ShoppingGoodsProductBean entity) {
		log.info("<ShoppingGoodsProductDaoImpl>-----<insert>------start");
		ShoppingGoodsProductBean insert = super.insert(entity);
		log.info("<ShoppingGoodsProductDaoImpl>-----<insert>------end");
		return insert;
	}

	/**
	 * 绑定商品列表查询
	 */
	public ReturnDataUtil list(ShoppingGoodsProductForm ShoppingGoodsProductForm) {
		log.info("<ShoppingGoodsProductDaoImpl>-----<list>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		int goodsId = ShoppingGoodsProductForm.getGoodsId();
		int isBind = ShoppingGoodsProductForm.getIsBind();
		String name = ShoppingGoodsProductForm.getName();
		sql.append(
				" select c.id,c.goodsId,g.name,g.id AS productId,c.quantity from (select id,name from item_basic   where 1=1   ");
		// 公司判断 该商品是否属于某一个公司
		if (ShoppingGoodsProductForm.getCompanyId() != null && ShoppingGoodsProductForm.getCompanyId() != 0) {
			/*if (ShoppingGoodsProductForm.getCompanyId() == 76) {*/
				sql.append(" and companyId in (1," + ShoppingGoodsProductForm.getCompanyId() + ") ");
			/*} else {
				sql.append(" and companyId=" + ShoppingGoodsProductForm.getCompanyId() + "  ");
			}*/
		}
		// 机器判断
		if (StringUtil.isNotBlank(ShoppingGoodsProductForm.getCode())) {
			sql.append(" and companyId in " + "( select companyId from vending_machines_info where code='"
					+ ShoppingGoodsProductForm.getCode() + "' ) ");
		}

		sql.append(
				" ) g left join (select id,goodsId,itemId,quantity from shopping_goods_product where  deleteFlag=0 and goodsId="
						+ goodsId + " ) c ON c.itemId=g.id where 1=1 ");

		if (isBind == 0) {// 未绑定
			sql.append(" and c.goodsId IS  NULL");
		} else if (isBind == 1) {// 1 已绑定
			sql.append(" and c.goodsId IS not NULL");
		}

		if (StringUtil.isNotBlank(name)) {
			sql.append(" and name like '%" + name.trim() + "%'");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("==绑定商品列表查询==sql:  " + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (ShoppingGoodsProductForm.getCurrentPage() - 1) * ShoppingGoodsProductForm.getPageSize();
			pst = conn
					.prepareStatement(sql.toString() + " limit " + off + "," + ShoppingGoodsProductForm.getPageSize());
			rs = pst.executeQuery();
			List<ShoppingGoodsProductVo> list = Lists.newArrayList();
			while (rs.next()) {
				ShoppingGoodsProductVo bean = new ShoppingGoodsProductVo();
				bean.setGoodId(rs.getInt("goodsId"));
				bean.setProductId(rs.getLong("productId"));
				bean.setProductName(rs.getString("name"));
				bean.setId(rs.getInt("id"));
				bean.setQuantity(rs.getInt("quantity"));
				if (rs.getInt("goodsId") == 0) {
					bean.setBindLabel("未绑定");
				} else {
					bean.setBindLabel("已绑定");
				}
				list.add(bean);
			}
			data.setCurrentPage(ShoppingGoodsProductForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ShoppingGoodsProductDaoImpl>-----<list>------end");
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsProductDaoImpl>-----<list>------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 根据商城商品id 查询绑定商品信息
	 */
	@Override
	public List<ShoppingGoodsProductBean> getShoppingGoodsProductBean(Long shoppingGoodsId) {
		log.info("<ShoppingGoodsProductDaoImpl>----<getShoppingGoodsProductBean>------start");
		StringBuilder sql = new StringBuilder();
		sql.append("select id,goodsId,itemId,itemName,createTime,createUser,updateTime,updateUser,deleteFlag,quantity  ");
		sql.append(" from shopping_goods_product where deleteFlag=0 and goodsId='"+shoppingGoodsId+"' ");
		List<ShoppingGoodsProductBean> list = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("根据商城商品id 查询绑定商品信息  sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ShoppingGoodsProductBean bean = new ShoppingGoodsProductBean();
				bean.setId(rs.getLong("id"));
				bean.setGoodsId(rs.getLong("goodsId"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setItemName(rs.getString("itemName"));;
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setQuantity(rs.getLong("quantity"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<ShoppingGoodsProductDaoImpl>----<getShoppingGoodsProductBean>------start");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsProductDaoImpl>----<getShoppingGoodsProductBean>------start");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
}
