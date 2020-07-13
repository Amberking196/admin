package com.server.module.system.warehouseManage.warehouseGiveBack;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemDaoImpl;
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
import java.util.List;

/**
 * author name: why create time: 2018-05-24 11:34:32
 */
@Repository
public class WarehouseGiveBackDaoImpl extends BaseDao<WarehouseGiveBackBean> implements WarehouseGiveBackDao {

	public static Logger log = LogManager.getLogger(WarehouseGiveBackDaoImpl.class);

	public ReturnDataUtil listPage(WarehouseGiveBackForm warehouseGiveBackForm) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select itemName,quantity,s.name typeName,operatorName,createTime  from  warehouse_give_back  w ");
		sql.append(" left join state_info s  on w.type=s.state WHERE billId ='"+warehouseGiveBackForm.getBillId()+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("归还查询列表sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (warehouseGiveBackForm.getCurrentPage() - 1) * warehouseGiveBackForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + warehouseGiveBackForm.getPageSize());
			rs = pst.executeQuery();
			List<WarehouseGiveBackBean> list = Lists.newArrayList();
			int number=0;
			while (rs.next()) {
				number++;
				WarehouseGiveBackBean bean = new WarehouseGiveBackBean();
				bean.setNumber(number);
				bean.setItemName(rs.getString("itemName"));
				bean.setQuantity(rs.getInt("quantity"));
				bean.setOperatorName(rs.getString("operatorName"));
				bean.setTypeName(rs.getString("typeName"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setTime(rs.getString("createTime").substring(0, rs.getString("createTime").length()-2));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(warehouseGiveBackForm.getCurrentPage());
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

	

	public WarehouseGiveBackBean insert(WarehouseGiveBackBean entity) {
		return super.insert(entity);
	}

	
}
