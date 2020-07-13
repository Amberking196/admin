package com.server.module.system.machineManage.machineItem;

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
 * author name: yjr create time: 2018-04-13 08:57:26
 */
@Repository
public class VendingMachinesItemDaoImpl extends BaseDao<VendingMachinesItemBean> implements VendingMachinesItemDao {


	public static Logger log = LogManager.getLogger(VendingMachinesItemDaoImpl.class); 

	public ReturnDataUtil listPage(VendingMachinesItemCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,companyId,basicItemId,price,endTime,costPrice,hot,updateTime,createTime from vending_machines_item where 1=1 ");
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
			List<VendingMachinesItemBean> list = Lists.newArrayList();
			while (rs.next()) {
				VendingMachinesItemBean bean = new VendingMachinesItemBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setBasicItemId(rs.getLong("basicItemId"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setEndTime(rs.getDate("endTime"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setHot(rs.getInt("hot"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setCreateTime(rs.getDate("createTime"));
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

	public VendingMachinesItemBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		VendingMachinesItemBean entity = new VendingMachinesItemBean();
		return super.del(entity);
	}

	public boolean update(VendingMachinesItemBean entity) {
		return super.update(entity);
	}

	public VendingMachinesItemBean insert(VendingMachinesItemBean entity) {
		return super.insert(entity);
	}

	public List<VendingMachinesItemBean> list(VendingMachinesItemCondition condition) {
		return null;
	}
}
