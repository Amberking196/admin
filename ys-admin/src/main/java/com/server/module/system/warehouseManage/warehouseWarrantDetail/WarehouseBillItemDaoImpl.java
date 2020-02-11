package com.server.module.system.warehouseManage.warehouseWarrantDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-17 03:15:57
 */
@Repository
public class WarehouseBillItemDaoImpl extends BaseDao<WarehouseBillItemBean> implements WarehouseBillItemDao {

	public static Logger log = LogManager.getLogger(WarehouseBillItemDaoImpl.class);

	/**
	 * 入库详情查询 
	 */
	public List<WarehouseBillItemBean> get(int billId) {
		log.info("<WarehouseWarrantDetailDaoImpl>------<get>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select wbi.id,wbi.itemId,wbi.barCode,wbi.itemName,s.name unitName,wbi.quantity,wbi.price,wbi.money,wbi.remark,averagePrice,wbi.purchaseItemId,wbi.purchaseQuantity ");
		sql.append(" from warehouse_bill_item wbi left join item_basic ib on wbi.itemId=ib.id ");
		sql.append(" left join state_info  s on ib.unit=s.state   WHERE  wbi.billId='" + billId + "'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<WarehouseBillItemBean> list = Lists.newArrayList();
		log.info("入库详情查询sql语句：》》" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			int num = 0;
			while (rs.next()) {
				num++;
				WarehouseBillItemBean bean = new WarehouseBillItemBean();
				bean.setNum(num);
				bean.setId(rs.getLong("id"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setItemName(rs.getString("itemName"));
				bean.setUnitName(rs.getString("unitName"));
				bean.setQuantity(rs.getInt("quantity"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setMoney(rs.getBigDecimal("money"));
				bean.setRemark(rs.getString("remark"));
				bean.setAveragePrice(rs.getBigDecimal("averagePrice"));
				bean.setPurchaseItemId(rs.getInt("purchaseItemId"));
				bean.setPurchaseQuantity(rs.getLong("purchaseQuantity"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<WarehouseWarrantDetailDaoImpl>------<get>-----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public boolean update(WarehouseBillItemBean entity) {
		return super.update(entity);
	}

	public WarehouseBillItemBean insert(WarehouseBillItemBean entity) {
		return super.insert(entity);
	}

	/**
	 * 事务控制
	 *@author why
	 *@date 2018年9月11日-上午9:48:51
	 *@param conn
	 *@param entity
	 *@return
	 */
	public WarehouseBillItemBean insertTransaction(Connection conn,WarehouseBillItemBean entity) {
		return (WarehouseBillItemBean) super.insert(conn,entity);
	}
	
	@Override
	public boolean delete(Object id) {
		log.info("<WarehouseBillItemDaoImpl>-----<delete>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" delete  from  warehouse_bill_item  where id ='" + id + "' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("删除出入库详情sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				log.info("<WarehouseBillItemDaoImpl>-----<delete>-----end");
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

	public boolean update(Connection conn, WarehouseBillItemBean entity) {

		return super.update(conn, entity);
	}

	public List<WarehouseBillItemBean> getByBillId(int billId) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select wbi.id,wbi.itemId,wbi.itemName,wbi.barCode,wbi.unitName,wbi.averagePrice,wbi.quantity,wbi.price,wbi.money,wbi.remark  ");
		sql.append(" from warehouse_bill_item wbi  WHERE  wbi.billId=" + billId);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<WarehouseBillItemBean> list = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				WarehouseBillItemBean bean = new WarehouseBillItemBean();
				bean.setId(rs.getLong("id"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setItemName(rs.getString("itemName"));
				bean.setQuantity(rs.getInt("quantity"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setMoney(rs.getBigDecimal("money"));
				bean.setRemark(rs.getString("remark"));
				bean.setAveragePrice(rs.getBigDecimal("averagePrice"));
				bean.setUnitName(rs.getString("unitName"));
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

	public ReturnDataUtil listPage(WarehouseBillItemForm condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select i.id,i.billId,i.itemId,i.itemName,i.barCode,i.quantity,i.price,i.money,i.createTime,i.unitName,i.remark,b.number,b.output from warehouse_bill_item i inner join warehouse_output_bill b on i.billId=b.id  where 1=1 ");
		
		if(StringUtils.isNotEmpty(condition.getCompanyId())){
			sql.append(" and b.companyId="+condition.getCompanyId());
		}
		if(StringUtils.isNotEmpty(condition.getWarehouseId())){
			sql.append(" and b.warehouseId="+condition.getWarehouseId());
		}
		if(StringUtils.isNotEmpty(condition.getItemId())){
			sql.append(" and i.itemId="+condition.getItemId());
		}
		if(StringUtils.isNotEmpty(condition.getOutput())){
			sql.append(" and b.output="+condition.getOutput());
		}
		if(StringUtils.isNotEmpty(condition.getNumber())){
			sql.append(" and b.number like '%"+condition.getNumber()+"%' ");
		}
		if(StringUtils.isNotEmpty(condition.getStartTime())){
			sql.append(" and i.createTime>='"+condition.getStartTime()+" 00:00:00'");
		}
		if(StringUtils.isNotEmpty(condition.getEndTime())){
			sql.append(" and i.createTime<='"+condition.getEndTime()+" 23:59:59'");
		}
		sql.append(" and (b.state=60203 || b.state=60403) ");//60203
		if (showSql) {
			log.info(sql);
		}
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
			List<WarehouseBillItemBean> list = Lists.newArrayList();
			while (rs.next()) {
				WarehouseBillItemBean bean = new WarehouseBillItemBean();
				bean.setId(rs.getLong("id"));
				bean.setBillId(((Long)rs.getLong("billId")).intValue());
				bean.setItemId(rs.getLong("itemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setQuantity(rs.getInt("quantity"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setMoney(rs.getBigDecimal("money"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
				bean.setOutput(rs.getInt("output"));
				bean.setNumber(rs.getString("number"));
				bean.setUnitName(rs.getString("unitName"));
				String outputLabel="";
				if(bean.getOutput()==0){
					outputLabel="入库";
				}
				if(bean.getOutput()==1){
					outputLabel="出库";
				}
				bean.setOutputLabel(outputLabel);
				list.add(bean);
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
	 * 事务控制 查询单据下的商品信息
	 */
	public List<WarehouseBillItemBean> getBillItemTransaction(Connection conn,int billId) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select wbi.id,wbi.itemId,wbi.itemName,wbi.barCode,wbi.unitName,wbi.averagePrice,wbi.quantity,wbi.price,wbi.money,wbi.remark  ");
		sql.append(" from warehouse_bill_item wbi  WHERE  wbi.billId=" + billId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("事务控制 查询单据下的商品信息 sql 语句"+sql.toString());
		List<WarehouseBillItemBean> list = Lists.newArrayList();
		try {
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				WarehouseBillItemBean bean = new WarehouseBillItemBean();
				bean.setId(rs.getLong("id"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setItemName(rs.getString("itemName"));
				bean.setQuantity(rs.getInt("quantity"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setMoney(rs.getBigDecimal("money"));
				bean.setRemark(rs.getString("remark"));
				bean.setAveragePrice(rs.getBigDecimal("averagePrice"));
				bean.setUnitName(rs.getString("unitName"));
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
			this.closeConnection(rs, pst, null);
		}
	}
}
