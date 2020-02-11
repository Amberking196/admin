package com.server.module.system.warehouseManage.stock;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.common.persistence.BaseDao;
import com.server.module.system.warehouseManage.checkLog.ItemInWayNumVo;
import com.server.module.system.warehouseManage.checkLog.OutNumVo;
import com.server.module.system.warehouseManage.checkLog.ReplenishNumVo;
import com.server.module.system.warehouseManage.checkLog.SellNumVo;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-05-22 10:49:58
 */
@Repository
public class WarehouseStockDaoImpl extends BaseDao<WarehouseStockBean> implements WarehouseStockDao {

	private static Logger log = LogManager.getLogger(WarehouseStockDaoImpl.class);

	@Autowired
	private CompanyDao companyDao;
    /*
    显示产品库存信息 ，仓库、公司两个维度，如果是公司要做分组统计
     */
	@SuppressWarnings("resource")
	public ReturnDataUtil listPage(WarehouseStockForm condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder(100);
		if (condition.getType() == 1) {// 按仓库展示库存
			sql.append(
					"select id,warehouseId,warehouseName,itemId,itemName,barCode,unitName,costPrice, quantity,purchaseNumber,price,lowerLimit,higherLimit from warehouse_stock where 1=1 ");

		} else if (condition.getType() == 2) {//按公司展示库存
			sql.append(
					" select companyId,companyName,itemId,itemName,barCode,unitName,sum(quantity) as quantity,SUM(quantity*costPrice) AS zongJiner from warehouse_stock where 1=1 ");
		}

		// sql.append("select
		// id,itemId,warehouseId,itemName,quantity,costPrice,price,createTime,remark
		// from warehouse_stock where 1=1 ");
		if (StringUtils.isNotEmpty(condition.getCompanyId())) {//查询某公司的库存
			sql.append(" and warehouseId in (select id from warehouse_info where companyId=" + condition.getCompanyId()
					+ ") ");

		}else {//只能查该用户所在公司的库存
			Integer companyId=UserUtils.getUser().getCompanyId();
			if(companyId!=1){
				sql.append(" and companyId in "+companyDao.findAllSonCompanyIdForInSql(companyId));
			}
		}
		if (condition.getWarehouseId()!=null) {
			sql.append(" and warehouseId=" + condition.getWarehouseId());
		}
		if (StringUtils.isNotEmpty(condition.getItemId())) {
			sql.append(" and itemId=" + condition.getItemId());

		}
		if (StringUtils.isNotEmpty(condition.getItemName())) {
			sql.append(" and itemName like '%" + condition.getItemName() + "%'");
		}
		if (condition.getType() == 1) {// 仓库
			// sql.append(" group by
			// warehouseId,warehouseName,itemId,itemName,costPrice ");

		} else if (condition.getType() == 2) {// 公司
			sql.append(" group by  companyId,companyName,itemId,itemName,barCode ");

		}

		if (showSql) {
			log.info(sql.toString());
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement("select count(*) from (" + sql.toString() + ") as c");
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			sql.append(" order by createTime desc");//排序
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

			rs = pst.executeQuery();
			List<WarehouseStockBean> list = Lists.newArrayList();
			while (rs.next()) {
				WarehouseStockBean bean = new WarehouseStockBean();
				if (condition.getType() == 1) {
					bean.setId(rs.getLong("id"));
					bean.setItemId(rs.getLong("itemId"));
					bean.setWarehouseId(rs.getLong("warehouseId"));
					bean.setWarehouseName(rs.getString("warehouseName"));
					bean.setItemName(rs.getString("itemName"));
					bean.setQuantity(rs.getLong("quantity"));
					bean.setCostPrice(rs.getDouble("costPrice"));
					bean.setPurchaseNumber(rs.getInt("purchaseNumber"));
					bean.setLowerLimit(rs.getInt("lowerLimit"));
					bean.setHigherLimit(rs.getInt("higherLimit"));
					bean.setUnitName(rs.getString("unitName"));
					bean.setPrice(rs.getDouble("price"));
					bean.setZongJiner(new BigDecimal(bean.getCostPrice() * bean.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					// bean.setCostPrice(rs.getDouble("costPrice"));
					// bean.setPrice(rs.getDouble("price"));
					// bean.setCreateTime(rs.getDate("createTime"));
					// bean.setRemark(rs.getString("remark"));
				}
				if (condition.getType() == 2) {
					// companyId,companyName,itemId,itemName

					bean.setItemId(rs.getLong("itemId"));
					bean.setCompanyId(rs.getLong("companyId"));
					bean.setCompanyName(rs.getString("companyName"));
					bean.setItemName(rs.getString("itemName"));
					bean.setQuantity(rs.getLong("quantity"));
					// bean.setCostPrice(rs.getDouble("costPrice"));
					bean.setZongJiner(new BigDecimal(((Double)rs.getDouble("zongJiner"))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

					BigDecimal z = new BigDecimal(bean.getZongJiner());
					BigDecimal q = new BigDecimal(bean.getQuantity());
					if (q.intValue() != 0) {
						BigDecimal p = z.divide(q, 2, BigDecimal.ROUND_HALF_UP);
						bean.setCostPrice(p.doubleValue());
					} else {
						bean.setCostPrice(0d);
					}
				}
				bean.setBarCode(rs.getString("barCode"));
				bean.setUnitName(rs.getString("unitName"));
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
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public WarehouseStockBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		WarehouseStockBean entity = new WarehouseStockBean();
		return super.del(entity);
	}

	public boolean update(WarehouseStockBean entity) {
		return super.update(entity);
	}

	public boolean update(Connection conn, WarehouseStockBean entity) {
		return super.update(conn, entity);
	}
    //插入
	public WarehouseStockBean insert(WarehouseStockBean entity) {
		return super.insert(entity);
	}
    //插入 传入conn 以实现事务
	public WarehouseStockBean insert(Connection conn, WarehouseStockBean entity) {
		return (WarehouseStockBean) super.insert(conn, entity);
	}

	public List<WarehouseStockBean> list(WarehouseStockForm condition) {
		return null;
	}

	@Override
	public WarehouseStockBean getStock(Long warehouseId, Long itemId) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,itemId,warehouseId,itemName,quantity,costPrice,price,createTime,remark,warehouseName,barCode from warehouse_stock where warehouseId="
						+ warehouseId + " and itemId=" + itemId);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<WarehouseStockBean> list = Lists.newArrayList();
			while (rs.next()) {
				WarehouseStockBean bean = new WarehouseStockBean();
				bean.setId(rs.getLong("id"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setWarehouseId(rs.getLong("warehouseId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCostPrice(rs.getDouble("costPrice"));
				bean.setPrice(rs.getDouble("price"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setBarCode(rs.getString("barCode"));
				list.add(bean);
			}
			if (list.size() > 0) {
				return list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 计算平均价格 基于仓库商品
	 * 
	 * @param warehouseId
	 * @param itemId
	 * @param curQuantity
	 *            当前商品的数量
	 * @param curPrice
	 *            当前商品金库价格
	 * @return
	 */
	public double getAveragePrice(Long warehouseId, Long itemId, int curQuantity, double curPrice) {

		String sql = "SELECT  i.price,i.quantity FROM  warehouse_bill_item i INNER JOIN warehouse_output_bill b ON i.billId=b.id AND  b.output=0 AND b.state=60203 and i.itemId="
				+ itemId + "  AND b.warehouseId=" + warehouseId;
		log.info(sql);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = Lists.newArrayList();

		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("price", rs.getDouble("price"));
				map.put("quantity", rs.getInt("quantity"));
				list.add(map);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		BigDecimal d = new BigDecimal(0);
		int quantity = 0;// 总数量
		log.info("size===" + list.size());
		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			int qu = (Integer) map.get("quantity");
			double price = (Double) map.get("price");
			quantity = quantity + qu;
			double totalPrice = price * qu;
			d = d.add(new BigDecimal(totalPrice));
		}
		quantity = quantity + curQuantity;
		d = d.add(new BigDecimal(curPrice * curQuantity));
		log.info("总金额==" + d.toString());
		BigDecimal avegrage = d.divide(new BigDecimal(quantity), 2, BigDecimal.ROUND_HALF_UP);
		log.info("平均价格==" + avegrage.doubleValue());
		return avegrage.doubleValue();

	}

	

	public static void main(String[] args) {
		BigDecimal d = new BigDecimal(0);

		BigDecimal d1 = new BigDecimal(1);
		d.divide(d1);

	}

	/**
	 * 查询仓库现有的商品 
	 */
	@Override
	public List<WarehouseStockBean> getItemInfo(Long warehouseId,Integer type) {
		log.info("<WarehouseStockDaoImpl>----<getItemInfo>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select itemId,barCode,itemName,unitName,quantity from warehouse_stock  where warehouseId="+warehouseId);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<WarehouseStockBean> list = Lists.newArrayList();
		log.info("查询仓库现有的商品 下拉框使用 -- sql语句"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				WarehouseStockBean bean = new WarehouseStockBean();
				bean.setItemId(rs.getLong("itemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setUnitName(rs.getString("unitName"));
				bean.setQuantity(rs.getLong("quantity"));
				list.add(bean);
			}
			log.info("<WarehouseStockDaoImpl>----<getItemInfo>----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<WarehouseStockDaoImpl>----<getItemInfo>----end");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public ReturnDataUtil checkOnlyOne(WarehouseStockBean bean) {
		log.info("<WarehouseStockDaoImpl>----<WarehouseStockBean>----start");
		StringBuilder sql = new StringBuilder();
		ReturnDataUtil data=new ReturnDataUtil();
		sql.append(" select itemId,barCode,itemName from warehouse_stock where itemId ="+bean.getItemId()+" and warehouseId="+bean.getWarehouseId());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("判断唯一性-- sql语句"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				data.setStatus(0);
			}
			log.info("<WarehouseStockDaoImpl>----<WarehouseStockBean>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<WarehouseStockDaoImpl>----<WarehouseStockBean>----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public ReturnDataUtil addItemToWarehouse(WarehouseStockBean bean) {
		log.info("<WarehouseStockDaoImpl>----<WarehouseStockBean>----start");
		StringBuilder sql = new StringBuilder();
		ReturnDataUtil data=new ReturnDataUtil();
		sql.append(" insert into warehouse_stock(itemId,warehouseId,itemName,createTime,companyId,companyName,warehouseName,barCode,purchaseNumber,lowerLimit,higherLimit,quantity,costPrice,price,unitName) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List list=new ArrayList();
		log.info("添加的商品到仓库中-- sql语句"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			//进行形参注入
			list.add(bean.getItemId());
			list.add(bean.getWarehouseId());
			list.add(bean.getItemName());
			list.add(bean.getCreateTime());
			list.add(bean.getCompanyId());
			list.add(bean.getCompanyName());
			list.add(bean.getWarehouseName());
			list.add(bean.getBarCode());
			list.add(bean.getPurchaseNumber());
			list.add(bean.getLowerLimit());
			list.add(bean.getHigherLimit());
			list.add(bean.getQuantity());
			list.add(bean.getCostPrice());
			list.add(bean.getPrice());
			list.add(bean.getUnitName());
			for(int i=0;i<list.size();i++) {
				pst.setObject(i+1, list.get(i));
			}
			int row = pst.executeUpdate();
			if(row!=0) {
				data.setStatus(0);
				data.setMessage("添加成功");
			}
			log.info("<WarehouseStockDaoImpl>----<WarehouseStockBean>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<WarehouseStockDaoImpl>----<WarehouseStockBean>----end");
			data.setStatus(-1);
			data.setMessage("添加失败");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	
	/**
	 * 根据商品id查询该商品的库存记录
	 */
	@Override
	public WarehouseStockBean getStockTransaction(Connection conn,Long warehouseId,Long itemId) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,itemId,warehouseId,itemName,quantity,costPrice,price,createTime,remark,warehouseName,barCode from warehouse_stock where warehouseId="
						+ warehouseId + " and itemId=" + itemId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("根据商品id查询该商品的库存记录 sql 语句"+sql.toString());
		try {
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<WarehouseStockBean> list = Lists.newArrayList();
			while (rs.next()) {
				WarehouseStockBean bean = new WarehouseStockBean();
				bean.setId(rs.getLong("id"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setWarehouseId(rs.getLong("warehouseId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCostPrice(rs.getDouble("costPrice"));
				bean.setPrice(rs.getDouble("price"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setBarCode(rs.getString("barCode"));
				list.add(bean);
			}
			if (list.size() > 0) {
				return list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, null);
		}
		return null;
	}

	@Override
	public List<WarehouseStockBean> getWarehouseItems(Long warehouseId, String barCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,itemId,warehouseId,itemName,quantity,remark,warehouseName,barCode from warehouse_stock where warehouseId="
						+ warehouseId + " and barCode like %" + barCode+"%");
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn=null;
		log.info(sql.toString());
		List<WarehouseStockBean> list = Lists.newArrayList();
		try {
			conn=openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			
			while (rs.next()) {
				WarehouseStockBean bean = new WarehouseStockBean();
				bean.setId(rs.getLong("id"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setWarehouseId(rs.getLong("warehouseId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCostPrice(rs.getDouble("costPrice"));
				bean.setPrice(rs.getDouble("price"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setBarCode(rs.getString("barCode"));
				list.add(bean);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	
	@Override
	public WarehouseStockBean getWarehouseItem(Long warehouseId, Long itemId) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,itemId,warehouseId,itemName,quantity,remark,warehouseName,barCode from warehouse_stock where warehouseId="
						+ warehouseId + " and itemId=" + itemId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn=null;
		log.info(sql.toString());
		List<WarehouseStockBean> list = Lists.newArrayList();
		try {
			conn=openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			
			while (rs.next()) {
				WarehouseStockBean bean = new WarehouseStockBean();
				bean.setId(rs.getLong("id"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setWarehouseId(rs.getLong("warehouseId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setRemark(rs.getString("remark"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setBarCode(rs.getString("barCode"));
				list.add(bean);
			}
			return list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list.get(0);
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	
	
}
