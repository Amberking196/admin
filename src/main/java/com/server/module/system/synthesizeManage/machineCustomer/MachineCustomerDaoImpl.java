package com.server.module.system.synthesizeManage.machineCustomer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.system.statisticsManage.merchandiseSalesStatistics.MerchandiseSalesStatisticsBean;
import com.server.module.system.synthesizeManage.roleManage.RoleBean;
import com.server.module.system.synthesizeManage.roleManage.RoleManageDaoImpl;
import com.server.module.system.userManage.CustomerBean;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class MachineCustomerDaoImpl extends BaseDao<CustomerBean> implements MachineCustomerDao{

	public static Logger log = LogManager.getLogger(MachineCustomerDaoImpl.class);
	
	@Override
	public ReturnDataUtil findCustomerByForm(TimeForm timeForm) {
		log.info("<MachineCustomerDaoImpl--findCustomerByForm--start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql = new StringBuffer();
		sql.append(" select tc.id,tc.phone,tc.createTime from tbl_customer as tc where tc.vmCode="+timeForm.getId());
		if(timeForm.getStartDate()!=null){
			sql.append(" and tc.createTime>='"+DateUtil.formatYYYYMMDDHHMMSS(timeForm.getStartDate())+"'");
		}
		if(timeForm.getEndDate()!=null){
			sql.append(" and tc.createTime<'"+DateUtil.formatLocalYYYYMMDDHHMMSS(timeForm.getEndDate(),1)+"'");
		}
		sql.append(" GROUP BY tc.createTime order by tc.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("sql>>>:"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getRow();
			}
			long off = (timeForm.getCurrentPage() - 1) * timeForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + timeForm.getPageSize());
			rs = pst.executeQuery();
			List<CustomerBean> list = Lists.newArrayList();
			int id=0;
			while (rs.next()) {
				id++;
				CustomerBean customer = new CustomerBean();
				customer = new CustomerBean();
				customer.setId(rs.getLong("id"));
				customer.setUserName(rs.getString("phone"));
				customer.setCreateTime(rs.getTimestamp("createTime"));
				list.add(customer);
			}
			data.setCurrentPage(timeForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setMessage("查询成功");
			log.info("<MachineCustomerDaoImpl--findCustomerByForm--end>");
			return data;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return data;
		} finally{
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
