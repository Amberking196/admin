package com.server.module.system.itemManage.TblStatisticsItemSale;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;

/**
 * author name: why create time: 2018-05-02 09:36:44
 */
@Repository
public class TblStatisticsItemSaleDaoImpl extends BaseDao<TblStatisticsItemSaleBean>
		implements TblStatisticsItemSaleDao {

	public static Logger log = LogManager.getLogger(TblStatisticsItemSaleDaoImpl.class); 
	
	public ReturnDataUtil listPage(TblStatisticsItemSaleCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,basicItemId,companyId,itemOnVmNum,reportDate,createTime,updateTime from tbl_statistics_item_sale where 1=1 ");
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
			List<TblStatisticsItemSaleBean> list = Lists.newArrayList();
			while (rs.next()) {
				TblStatisticsItemSaleBean bean = new TblStatisticsItemSaleBean();
				bean.setId(rs.getLong("id"));
				bean.setBasicItemId(rs.getLong("basicItemId"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setItemOnVmNum(rs.getLong("itemOnVmNum"));
				bean.setReportDate(rs.getString("reportDate"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
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

	public TblStatisticsItemSaleBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		TblStatisticsItemSaleBean entity = new TblStatisticsItemSaleBean();
		return super.del(entity);
	}

	public boolean update(TblStatisticsItemSaleBean entity) {
		return super.update(entity);
	}

	public TblStatisticsItemSaleBean insert(TblStatisticsItemSaleBean entity) {
		return super.insert(entity);
	}

	public List<TblStatisticsItemSaleBean> list(TblStatisticsItemSaleCondition condition) {
		return null;
	}

	/**
	 * 每天去查询商品的上架数
	 */
	@Override
	public List<TblStatisticsItemSaleBean> getItemSaleBean() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(1) num ,ss.basicItemId,ss.name,ss.companyId from " ); 
		sql.append(" ( select  count(1) as num,ib.`id` as basicItemId,ib.`name` as `name`,companyId " );
		sql.append(" from `vending_machines_way` as vmw " ); 
		sql.append(" inner join vending_machines_item as vmi on  vmw.`itemId` = vmi.`id` " ); 
		sql.append(" inner join item_basic as ib ON ib.id = vmi.`basicItemId` " ); 
		sql.append(" group by basicItemId,vendingMachinesCode) as ss " ); 
		sql.append(" group by ss.basicItemId ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<TblStatisticsItemSaleBean> list = Lists.newArrayList();
			while (rs.next()) {
				TblStatisticsItemSaleBean bean = new TblStatisticsItemSaleBean();
				bean.setBasicItemId(rs.getLong("basicItemId"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setItemOnVmNum(rs.getLong("num"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
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
