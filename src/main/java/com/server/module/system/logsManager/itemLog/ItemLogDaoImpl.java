package com.server.module.system.logsManager.itemLog;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.common.persistence.Page;
import com.server.dbpool.DBPool;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * author name: yjr create time: 2018-03-24 09:24:43
 */
@Repository
public class ItemLogDaoImpl extends BaseDao<ItemLogBean> implements ItemLogDao {


	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class);   
    @Autowired
	private CompanyDao companyDaoImpl;
	
	@SuppressWarnings("resource")
	public ReturnDataUtil listPage(ItemLogCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,vmCode,machinesItemId,itemName,barCode,price,costPrice,endTime,createTime,operator,oldPrice,oldCostPrice,oldEndTime from item_log where 1=1 ");
     if (condition.getCompanyId() == null || condition.getCompanyId()<=1 ){ //总公司或超级管理员
			
			
		} else {
			String insql=companyDaoImpl.findAllSonCompanyIdForInSql(condition.getCompanyId());
		    sql.append(" and vmCode in (select code from  vending_machines_info where companyId in "+insql+") ");
		}
		List<Object> plist=Lists.newArrayList();
		if (StringUtil.isNotBlank(condition.getVmCode())){
			sql.append(" and vmCode like ?");
			plist.add("%"+condition.getVmCode()+"%");
		}
		
		if (StringUtil.isNotBlank(condition.getItemName())){
			sql.append(" and itemName like ?");
			plist.add("%"+condition.getItemName()+"%");
		}
		
		if (StringUtil.isNotBlank(condition.getBarCode())){
			sql.append(" and barCode like ?");
			plist.add("%"+condition.getBarCode()+"%");
		}
		if (StringUtil.isNotBlank(condition.getOperator())){
			sql.append(" and operator like ?");
			plist.add("%"+condition.getOperator()+"%");
		}
		
		if (condition.getStartTime()!=null){
			sql.append(" and createTime >= ?");
			plist.add(condition.getStartTime());
		}
		
		if (condition.getEndTime()!=null){
			sql.append(" and createTime <= ?");
			plist.add(condition.getEndTime());
		}
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
			List<ItemLogBean> list = Lists.newArrayList();
			while (rs.next()) {
				ItemLogBean bean = new ItemLogBean();
				bean.setId(rs.getLong("id"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setMachinesItemId(rs.getLong("machinesItemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setPrice(new BigDecimal(rs.getDouble("price")));
				bean.setCostPrice(new BigDecimal(rs.getDouble("costPrice")));
				bean.setEndTime(rs.getDate("endTime"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setOperator(rs.getString("operator"));
				bean.setOldPrice(new BigDecimal(rs.getDouble("oldPrice")));
				bean.setOldCostPrice(new BigDecimal(rs.getDouble("oldCostPrice")));
				bean.setOldEndTime(rs.getDate("oldEndTime"));
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

}
