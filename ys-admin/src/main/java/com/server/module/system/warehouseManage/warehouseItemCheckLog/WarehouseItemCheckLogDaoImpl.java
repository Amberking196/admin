package com.server.module.system.warehouseManage.warehouseItemCheckLog;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;

/**
 * author name: why create time: 2018-06-15 14:00:02
 */
@Repository
public class WarehouseItemCheckLogDaoImpl extends BaseDao<WarehouseItemCheckLogBean> implements WarehouseItemCheckLogDao {

	private static Logger log = LogManager.getLogger(WarehouseItemCheckLogDaoImpl.class);
	@Autowired
	private CompanyDao companyDaoImpl;

	public ReturnDataUtil listPage(WarehouseItemCheckLogForm warehouseItemCheckLogForm) {
		log.info("<MerchandiseInventoryDaoImpl>----<listPage>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select  d.startQuantity,b.itemName,b.inQuantity,b.otherQuantity,b.outQuantity,b.createTime  from ");
		sql.append(" (select wbi.itemId,wbi.itemName,wob.companyId,wob.warehouseId, SUM(case when wob.state='60203' then wbi.quantity end) as inQuantity,");
		sql.append(" SUM(case when wob.state='60403' and type!='60406' then wbi.quantity end) as outQuantity, ");
		sql.append(" SUM(case when wob.state='60403' and type='60406' then wbi.quantity end) as otherQuantity,wob.createTime ");
		sql.append(" from  warehouse_bill_item  wbi inner join warehouse_output_bill wob on wbi.billId = wob.id ");
		if (warehouseItemCheckLogForm.getStartTime() != null) {
			sql.append(" and wob.createTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(warehouseItemCheckLogForm.getStartTime())
					+ "' ");
		}
		if (warehouseItemCheckLogForm.getEndTime() != null) {
			sql.append(" and wob.createTime < '"
					+ DateUtil.formatLocalYYYYMMDDHHMMSS(warehouseItemCheckLogForm.getEndTime(), 1) + "' ");
		}
		sql.append(" group by wbi.itemId,wob.warehouseId,wob.companyId ) ");
		sql.append(" b left  join  (select  a.itemId,a.companyId,a.startQuantity,a.createTime from warehouse_item_check_log as a inner join ( ");
		sql.append(" select MAX(createTime) as  createTime,itemId,companyId from warehouse_item_check_log where ");
		if (warehouseItemCheckLogForm.getStartTime() != null) {
			sql.append("  createTime <= '"
					+ DateUtil.formatLocalYYYYMMDDHHMMSS(warehouseItemCheckLogForm.getStartTime(), 1) + "' ");
		}
		sql.append(" group by itemId,companyId ) as c  on a.createTime = c.createTime and a.itemId = c.itemId and a.companyId = c.companyId ) d ");
		sql.append(" on b.itemId=d.itemId  and b.companyId=d.companyId where  b.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		if(warehouseItemCheckLogForm.getWarehouseId()!=null){
			sql.append("and b.warehouseId ='"+warehouseItemCheckLogForm.getWarehouseId()+"' ");
		}
		sql.append( " order by  d.createTime desc,d.startQuantity desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("type1商品盘点sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (warehouseItemCheckLogForm.getCurrentPage() - 1) * warehouseItemCheckLogForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + warehouseItemCheckLogForm.getPageSize());
			rs = pst.executeQuery();
			List<WarehouseItemCheckLogBean> list = Lists.newArrayList();
			int num=0;
			while (rs.next()) {
				num++;
				WarehouseItemCheckLogBean bean = new WarehouseItemCheckLogBean();
				bean.setNum(num);
				bean.setQuantity(rs.getInt("startQuantity"));
				bean.setItemName(rs.getString("itemName"));
				bean.setInQuantity(rs.getInt("inQuantity"));
				bean.setOutQuantity(rs.getInt("outQuantity"));
				bean.setOtherQuantity(rs.getInt("otherQuantity"));
				int all=rs.getInt("startQuantity") + rs.getInt("inQuantity");
				int endQuantity=all-rs.getInt("outQuantity")-rs.getInt("otherQuantity");
				bean.setEndQuantity(endQuantity);
				bean.setCreateTime(rs.getDate("createTime"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(warehouseItemCheckLogForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<MerchandiseInventoryDaoImpl>----<listPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<MerchandiseInventoryDaoImpl>----<listPage>----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	
	public ReturnDataUtil newListPage(WarehouseItemCheckLogForm warehouseItemCheckLogForm) {
		log.info("<MerchandiseInventoryDaoImpl>----<newListPage>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT sum(d.startQuantity) as startQuantity,sum(d.startMoney) as startMoney,d.itemName,d.itemid,sum(b.inQuantity) as inQuantity,sum(b.otherQuantity) as otherQuantity,sum(b.outQuantity) as outQuantity,b.createTime");
		sql.append(" ,sum(b.inMoney) as inMoney,sum(b.otherMoney) as otherMoney,sum(b.outMoney) as outMoney ");
		sql.append(" from");
		sql.append(" ( select  wic.startMoney,wic.itemName,wic.itemId,wic.companyId,wic.warehouseId,wic.startQuantity,wic.createTime from warehouse_item_check_log wic inner join ( ");
		sql.append(" select MAX(createTime) as  createTime,itemId,companyId from warehouse_item_check_log where 1=1 ");
		if (warehouseItemCheckLogForm.getStartTime() != null) {
			sql.append("  and createTime <= '"
							+ DateUtil.formatLocalYYYYMMDDHHMMSS(warehouseItemCheckLogForm.getStartTime(), 1) + "' ");
			}
		sql.append(" group by itemId,companyId )  c  on wic.createTime = c.createTime and wic.itemId = c.itemId and wic.companyId = c.companyId  )  d  ");
		sql.append(" LEFT JOIN ");
		sql.append(" (select wbi.itemId,wbi.itemName,wob.companyId, SUM(case when wob.state='60203' then wbi.quantity end) as inQuantity,");
		sql.append(" SUM(case when wob.state='60403' and type!='60406' then wbi.quantity end) as outQuantity, ");
		sql.append(" SUM(case when wob.state='60403' and type='60406' then wbi.quantity end) as otherQuantity,wob.createTime, ");
		
		sql.append(" SUM(case when wob.state='60203' then wbi.money end) as inMoney,");
		sql.append(" SUM(case when wob.state='60403' and type!='60406' then wbi.money end) as outMoney, ");
		sql.append(" SUM(case when wob.state='60403' and type='60406' then wbi.money end) as otherMoney ");

		sql.append(" from  warehouse_bill_item  wbi inner join warehouse_output_bill wob on wbi.billId = wob.id ");
		if (warehouseItemCheckLogForm.getStartTime() != null) {
			sql.append(" and wob.createTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(warehouseItemCheckLogForm.getStartTime())
					+ "' ");
		}
		if (warehouseItemCheckLogForm.getEndTime() != null) {
			sql.append(" and wob.createTime < '"
					+ DateUtil.formatLocalYYYYMMDDHHMMSS(warehouseItemCheckLogForm.getEndTime(), 1) + "' ");
		}
		sql.append(" group by wbi.itemId,wob.companyId ) b ON b.itemId = d.itemId AND b.companyId = d.companyId where  d.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
        if(warehouseItemCheckLogForm.getWarehouseId()!=null){
            sql.append("and d.warehouseId ='"+warehouseItemCheckLogForm.getWarehouseId()+"' ");
        }
	
		sql.append(" GROUP by d.itemid ");
		sql.append(" order by d.startQuantity desc,itemId DESC ");

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("TYPE2商品盘点sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countGroupSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
				log.info("--"+count);
			}
			long off = (warehouseItemCheckLogForm.getCurrentPage() - 1) * warehouseItemCheckLogForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + warehouseItemCheckLogForm.getPageSize());
			rs = pst.executeQuery();
			List<WarehouseItemCheckLogBean> list = Lists.newArrayList();
			int num=0;
			while (rs.next()) {
				num++;
				WarehouseItemCheckLogBean bean = new WarehouseItemCheckLogBean();
				bean.setNum(num);
				bean.setQuantity(rs.getInt("startQuantity"));
				bean.setItemName(rs.getString("itemName"));
				bean.setInQuantity(rs.getInt("inQuantity"));
				bean.setOutQuantity(rs.getInt("outQuantity"));
				bean.setOtherQuantity(rs.getInt("otherQuantity"));
				int all=rs.getInt("startQuantity") + rs.getInt("inQuantity");
				int endQuantity=all-rs.getInt("outQuantity")-rs.getInt("otherQuantity");
				bean.setEndQuantity(endQuantity);
				bean.setCreateTime(rs.getDate("createTime"));
				
				bean.setStartMoney(rs.getBigDecimal("startMoney")!=null?rs.getBigDecimal("startMoney"):new BigDecimal(0));
				bean.setInMoney(rs.getBigDecimal("inMoney")!=null?rs.getBigDecimal("inMoney"):new BigDecimal(0));
				bean.setOutMoney(rs.getBigDecimal("outMoney")!=null?rs.getBigDecimal("outMoney"):new BigDecimal(0));
				bean.setOtherMoney(rs.getBigDecimal("otherMoney")!=null?rs.getBigDecimal("otherMoney"):new BigDecimal(0));
				//bean.getMoney(rs.getBigDecimal("otherMoney"));
				bean.setEndMoney(bean.getStartMoney().add(bean.getInMoney()).subtract(bean.getOutMoney()).subtract(bean.getOtherMoney()));

				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(warehouseItemCheckLogForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<MerchandiseInventoryDaoImpl>----<newListPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<MerchandiseInventoryDaoImpl>----<newListPage>----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
}
