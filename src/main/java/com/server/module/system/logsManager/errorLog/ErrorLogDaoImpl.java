package com.server.module.system.logsManager.errorLog;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.common.persistence.Page;
import com.server.dbpool.DBPool;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

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
 * author name: yjr create time: 2018-03-24 09:57:07
 */
@Repository
public class ErrorLogDaoImpl extends BaseDao<ErrorLogBean> implements ErrorLogDao {


	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class); 
    @Autowired
	private CompanyDao companyDaoImpl;
	@SuppressWarnings("resource")
	public ReturnDataUtil listPage(ErrorLogCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select a.id,a.state,a.orderId,a.vendingMachinesCode,a.msg,a.createTime,a.solveTime,a.solveState,a.solve,a.remark from error_log a left join vending_machines_info b on a.vendingMachinesCode=b.code where 1=1 ");
		List<Object> plist = Lists.newArrayList();
		//left join vending_machines_info b on a.vendingMachinesCode=b.code
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (StringUtil.isNotBlank(condition.getVmCode())) {
			sql.append(" and a.vendingMachinesCode like ?");
			plist.add("%" + condition.getVmCode() + "%");
		}
		if (condition.getState() != null) {
			sql.append(" and a.state = ?");
			plist.add(condition.getState());
		}
		Integer companyId=null;
		if (condition.getCompanyId()!=null){
			companyId=condition.getCompanyId();
		}else{
			 companyId=UserUtils.getUser().getCompanyId();
		}
		sql.append(" and b.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(companyId));

		sql.append(" order by a.createTime desc");
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
			List<ErrorLogBean> list = Lists.newArrayList();
			while (rs.next()) {
				ErrorLogBean bean = new ErrorLogBean();
				bean.setId(rs.getLong("id"));
				bean.setState(rs.getInt("state"));
				bean.setOrderId(rs.getLong("orderId"));
				bean.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				bean.setMsg(rs.getString("msg"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setSolveTime(rs.getDate("solveTime"));
				bean.setSolveState(rs.getInt("solveState"));
				bean.setSolve(rs.getString("solve"));
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
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (conn != null)
					closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean update(ErrorLogBean entity) {
		return super.update(entity);
	}

}
