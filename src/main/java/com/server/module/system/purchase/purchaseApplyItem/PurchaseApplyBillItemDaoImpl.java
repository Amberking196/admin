package com.server.module.system.purchase.purchaseApplyItem;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author name: yjr create time: 2018-08-31 17:41:12
 */
@Repository
public class PurchaseApplyBillItemDaoImpl extends BaseDao<PurchaseApplyBillItemBean>
		implements PurchaseApplyBillItemDao {

	private static Log log = LogFactory.getLog(PurchaseApplyBillItemDaoImpl.class);

	public ReturnDataUtil listPage(PurchaseApplyBillItemCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,billId,itemId,itemName,barCode,unitName,quantity,createTime,remark from purchase_apply_bill_item where 1=1 ");
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
			List<PurchaseApplyBillItemBean> list = Lists.newArrayList();
			while (rs.next()) {
				PurchaseApplyBillItemBean bean = new PurchaseApplyBillItemBean();
				bean.setId(rs.getLong("id"));
				bean.setBillId(rs.getLong("billId"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setUnitName(rs.getString("unitName"));
				bean.setQuantity(rs.getInt("quantity"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
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

	public PurchaseApplyBillItemBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		PurchaseApplyBillItemBean entity = new PurchaseApplyBillItemBean();
		return super.del(entity);
	}

	public boolean update(PurchaseApplyBillItemBean entity) {
		return super.update(entity);
	}

	public PurchaseApplyBillItemBean insert(PurchaseApplyBillItemBean entity) {
		return super.insert(entity);
	}

	public List<PurchaseApplyBillItemBean> list(PurchaseApplyBillItemCondition condition) {
		return null;
	}

	@Override
	public ReturnDataUtil addItem(Connection conn, PurchaseApplyBillItemBean entity) throws SQLException {
		log.info("<PurchaseApplyBillItemDaoImpl>------<addItem>----start");
		// 保存数据
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql=new StringBuffer();
		PreparedStatement pst=null;
		sql.append(" insert into purchase_apply_bill_item (billId,itemId,itemName,barCode,unitName,quantity,price,remark) values(?,?,?,?,?,?,?,?)");
		
			pst = conn.prepareStatement(sql.toString());
			List plist=new ArrayList();
			plist.add(entity.getBillId());
			plist.add(entity.getItemId());
			plist.add(entity.getItemName());
			plist.add(entity.getBarCode());
			plist.add(entity.getUnitName());
			plist.add(entity.getQuantity());
			plist.add(entity.getPrice());
			plist.add(entity.getRemark());
			for(int i=0;i<plist.size();i++) {
				pst.setObject(i+1, plist.get(i));
			}
			pst.executeUpdate();
		log.info("<PurchaseApplyBillItemDaoImpl>------<addItem>----end");
		return data;
	}

	@Override
	public ReturnDataUtil findItemsById(Integer id) {
		log.info("<PurchaseApplyBillItemDaoImpl>------<findItemsById>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,billId,itemId,itemName,barCode,unitName,quantity,createTime,remark from purchase_apply_bill_item where 1=1 and billId="+id);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			 pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<PurchaseApplyBillItemBean> list = Lists.newArrayList();
			while (rs.next()) {
				PurchaseApplyBillItemBean bean = new PurchaseApplyBillItemBean();
				bean.setId(rs.getLong("id"));
				bean.setBillId(rs.getLong("billId"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setUnitName(rs.getString("unitName"));
				bean.setQuantity(rs.getInt("quantity"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
				bean.setRealQuantity(rs.getInt("quantity"));
				list.add(bean);
				data.setStatus(0);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<PurchaseApplyBillItemDaoImpl>------<findItemsById>----end");
			data.setReturnObject(list);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			data.setStatus(-1);
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

	@Override
	public void update(PurchaseApplyBillItemBean purchaseApplyBillItemBean, Connection conn) throws SQLException {
		log.info("<PurchaseApplyBillItemDaoImpl>------<update>----start");
		// 保存数据
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql=new StringBuffer();
		PreparedStatement pst=null;
		sql.append("update purchase_apply_bill_item set supplierId=? where id=?");
			pst = conn.prepareStatement(sql.toString());
			List plist=new ArrayList();
			plist.add(purchaseApplyBillItemBean.getSupplierId());
			plist.add(purchaseApplyBillItemBean.getId());
			for(int i=0;i<plist.size();i++) {
				pst.setObject(i+1, plist.get(i));
			}
			pst.executeUpdate();
		log.info("<PurchaseApplyBillItemDaoImpl>------<update>----end");
		
	}
}
