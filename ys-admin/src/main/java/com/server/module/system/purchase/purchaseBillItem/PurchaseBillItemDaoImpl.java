package com.server.module.system.purchase.purchaseBillItem;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author name: yjr create time: 2018-09-03 16:27:30
 */
@Repository
public class PurchaseBillItemDaoImpl extends BaseDao<PurchaseBillItemBean> implements PurchaseBillItemDao {

	private static Logger log = LogManager.getLogger(PurchaseBillItemDaoImpl.class);

	public ReturnDataUtil listPage(PurchaseBillItemForm condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,billId,itemId,itemName,barCode,unitName,supplierId,applyQuantity,quantity,storageQuantity,storageState,createTime,remark,price from purchase_bill_item where 1=1 ");		
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
			rs = pst.executeQuery();
			List<PurchaseBillItemBean> list = Lists.newArrayList();
			while (rs.next()) {
				PurchaseBillItemBean bean = new PurchaseBillItemBean();
				bean.setId(rs.getLong("id"));
				bean.setBillId(rs.getLong("billId"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setUnitName(rs.getString("unitName"));
				bean.setSupplierId(rs.getLong("supplierId"));
				bean.setApplyQuantity(rs.getLong("applyQuantity"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setStorageQuantity(rs.getLong("storageQuantity"));
				bean.setStorageState(rs.getInt("storageState"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
				bean.setPrice(rs.getDouble("price"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
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

	
	/**
	 * 根据id查询商品信息
	 */
	public PurchaseBillItemBean get(Object id) {
		log.info("<PurchaseBillItemDaoImpl>-----<get>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		PurchaseBillItemBean bean=null;
		sql.append(" select pbl.id ,pbl.billId,pbl.itemId,pbl.itemName,pbl.barCode,pbl.unitName,pbl.supplierId,pbl.applyQuantity,pbl.quantity,pbl.storageQuantity,pbl.storageState,pbl.createTime,pbl.remark,pbl.price  ");
		sql.append("  from   purchase_bill_item pbl    where id='"+id+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("根据id查询商品信息 sql:"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean=new PurchaseBillItemBean();
				bean.setId(rs.getLong("Id"));
				bean.setBillId(rs.getLong("billId"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setUnitName(rs.getString("unitName"));
				bean.setSupplierId(rs.getLong("supplierId"));
				bean.setApplyQuantity(rs.getLong("applyQuantity"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setStorageQuantity(rs.getLong("storageQuantity"));
				bean.setStorageState(rs.getInt("storageState"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
				bean.setPrice(rs.getDouble("price"));
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<PurchaseBillItemDaoImpl>-----<get>-----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<PurchaseBillItemDaoImpl>-----<get>-----end");
			return bean;
		} finally {
				this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 根据id查询商品信息
	 */
	public PurchaseBillItemBean getTransaction(Connection conn,Object id) {
		log.info("<PurchaseBillItemDaoImpl>-----<get>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		PurchaseBillItemBean bean=null;
		sql.append(" select pbl.id ,pbl.billId,pbl.itemId,pbl.itemName,pbl.barCode,pbl.unitName,pbl.supplierId,pbl.applyQuantity,pbl.quantity,pbl.storageQuantity,pbl.storageState,pbl.createTime,pbl.remark,pbl.price  ");
		sql.append("  from   purchase_bill_item pbl    where id='"+id+"' ");
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("根据id查询商品信息 sql:"+sql.toString());
		try {
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean=new PurchaseBillItemBean();
				bean.setId(rs.getLong("Id"));
				bean.setBillId(rs.getLong("billId"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setUnitName(rs.getString("unitName"));
				bean.setSupplierId(rs.getLong("supplierId"));
				bean.setApplyQuantity(rs.getLong("applyQuantity"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setStorageQuantity(rs.getLong("storageQuantity"));
				bean.setStorageState(rs.getInt("storageState"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
				bean.setPrice(rs.getDouble("price"));
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<PurchaseBillItemDaoImpl>-----<get>-----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<PurchaseBillItemDaoImpl>-----<get>-----end");
			return bean;
		} finally {
				this.closeConnection(rs, pst, null);
		}
	}
	
	/**
	 * 删除采购单商品
	 */
	public boolean delete(Object id) {
		log.info("<PurchaseBillItemDaoImpl>----<delete>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  purchase_bill_item set deleteFlag= 1 where id in ("+id+") ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("删除采购单商品sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<PurchaseBillItemDaoImpl>-----<delete>-----end");
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

	public boolean update(PurchaseBillItemBean entity) {
		log.info("<PurchaseBillItemDaoImpl>-----<update>-----start");
		Connection conn = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" update  purchase_bill_item set storageQuantity=storageQuantity+"+entity.getStorageQuantity()+" , storageState="+entity.getStorageState()+"  where id='"+entity.getId()+"' ");
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("事务 入库成功后 修改商品数量sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<PurchaseBillItemDaoImpl>-----<update>-----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, null);
		}
		return false;
	}
	/**
	 * 事务控制
	 * 入库成功后 修改商品入库数量
	 */
	public boolean updateTransaction(Connection conn,PurchaseBillItemBean entity) {
		log.info("<PurchaseBillItemDaoImpl>-----<update>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  purchase_bill_item set storageQuantity=storageQuantity+"+entity.getStorageQuantity()+" , storageState="+entity.getStorageState()+"  where id='"+entity.getId()+"' ");
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("事务 入库成功后 修改商品数量sql语句："+sql.toString());
		try {
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<PurchaseBillItemDaoImpl>-----<update>-----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, null);
		}
		return false;
	}

	public PurchaseBillItemBean insert(PurchaseBillItemBean entity) {
		return super.insert(entity);
	}

	/**
	 * 根据采购单id 查询采购单的商品
	 */
	public List<PurchaseBillItemBean> list(Long billId) {
		log.info("<PurchaseBillItemDaoImpl>----<list>-------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		List<PurchaseBillItemBean> list = Lists.newArrayList();
		sql.append(" select pbl.id ,pbl.billId,pbl.itemId,pbl.itemName,pbl.barCode,pbl.unitName,pbl.supplierId,pbl.applyQuantity,pbl.quantity,pbl.storageQuantity,pbl.storageState,pbl.createTime,pbl.remark,pbl.price,s.companyName supplierName  ");
		sql.append("  from   purchase_bill_item pbl  left join supplier  s on pbl.supplierId=s.id  where pbl.billId='"+billId+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("根据采购单id 查询采购单的商品 sql:"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				PurchaseBillItemBean bean=new PurchaseBillItemBean();
				bean.setId(rs.getLong("Id"));
				bean.setBillId(rs.getLong("billId"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setUnitName(rs.getString("unitName"));
				bean.setSupplierId(rs.getLong("supplierId"));
				bean.setApplyQuantity(rs.getLong("applyQuantity"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setStorageQuantity(rs.getLong("storageQuantity"));
				bean.setStorageState(rs.getInt("storageState"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
				bean.setPrice(rs.getDouble("price"));
				bean.setSupplierName(rs.getString("supplierName"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<PurchaseBillItemDaoImpl>----<list>------end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<PurchaseBillItemDaoImpl>----<list>------end");
			return list;
		} finally {
				this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public ReturnDataUtil addItem(PurchaseBillItemBean billBean, Connection conn) throws SQLException {
		log.info("<PurchaseApplyBillItemDaoImpl>------<addItem>----start");
		// 保存数据
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql=new StringBuffer();
		PreparedStatement pst=null;
		sql.append(" insert into purchase_bill_item (billId,itemId,itemName,barCode,unitName,quantity,supplierId,applyQuantity,storageQuantity,remark,storageState,price) values(?,?,?,?,?,?,?,?,?,?,?,?)");
			pst = conn.prepareStatement(sql.toString());
			List plist=new ArrayList();
			plist.add(billBean.getBillId());
			plist.add(billBean.getItemId());
			plist.add(billBean.getItemName());
			plist.add(billBean.getBarCode());
			plist.add(billBean.getUnitName());
			plist.add(billBean.getQuantity());
			plist.add(billBean.getSupplierId());
			plist.add(billBean.getApplyQuantity());
			plist.add(billBean.getStorageQuantity());
			plist.add(billBean.getRemark());
			plist.add(billBean.getStorageState());
			plist.add(billBean.getPrice());
			for(int i=0;i<plist.size();i++) {
				pst.setObject(i+1, plist.get(i));
			}
			pst.executeUpdate();
			data.setStatus(0);
		log.info("<PurchaseApplyBillItemDaoImpl>------<addItem>----end");
		return data;
	}
	
	public ReturnDataUtil successListPage(PurchaseBillItemForm purchaseBillItemForm) {
		log.info("<PurchaseBillItemDaoImpl>----<successListPage>-------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select s.companyName as supplierName,pbi.id,pbi.billId,itemId,itemName,barCode,unitName,supplierId,quantity,pbi.createTime,pbi.remark,pbi.price,pb.applyNumber from purchase_bill_item pbi  ");		
		sql.append(" inner join purchase_bill pb on pb.id=pbi.billId");
		sql.append(" inner join supplier s on s.id=pbi.supplierId");
		sql.append(" where 1=1 and pb.state != 1");

        if(purchaseBillItemForm.getItemId()!=null){
            sql.append(" and pbi.itemId="+purchaseBillItemForm.getItemId());
        }

        if(purchaseBillItemForm.getSupplierId()!=null){
            sql.append(" and pbi.supplierId="+purchaseBillItemForm.getSupplierId());
        }
		if(purchaseBillItemForm.getStartTime()!=null){
			sql.append(" and pbi.createTime>='"+ DateUtil.formatYYYYMMDDHHMMSS(purchaseBillItemForm.getStartTime())+" 00:00:00'");
		}
		if(purchaseBillItemForm.getEndTime()!=null){
			sql.append(" and pbi.createTime<='"+DateUtil.formatYYYYMMDDHHMMSS(purchaseBillItemForm.getEndTime())+" 23:59:59'");
		}
        sql.append(" order by pbi.createTime");

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if(purchaseBillItemForm.getIsShowAll()==1) {
				pst = conn.prepareStatement(sql.toString());
			}else {
				long off = (purchaseBillItemForm.getCurrentPage() - 1) * purchaseBillItemForm.getPageSize();
				pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + purchaseBillItemForm.getPageSize());
			}
			rs = pst.executeQuery();
			List<PurchaseBillItemBean> list = Lists.newArrayList();
			while (rs.next()) {
				PurchaseBillItemBean bean = new PurchaseBillItemBean();
				bean.setId(rs.getLong("id"));
				//bean.setBillId(rs.getLong("billId"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setUnitName(rs.getString("unitName"));
				bean.setSupplierId(rs.getLong("supplierId"));
				//bean.setApplyQuantity(rs.getLong("applyQuantity"));
				bean.setQuantity(rs.getLong("quantity"));
				//bean.setStorageQuantity(rs.getLong("storageQuantity"));
				//bean.setStorageState(rs.getInt("storageState"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
				bean.setPrice(rs.getDouble("price"));
				bean.setSupplierName(rs.getString("supplierName"));
				bean.setApplyNumber(rs.getString("applyNumber"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(purchaseBillItemForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<PurchaseBillItemDaoImpl>----<successListPage>-------end");
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

}
