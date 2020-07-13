package com.server.module.system.machineManage.machinesBadOpenLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class MachinesBadOpenDaoImpl extends MySqlFuns implements MachinesBadOpenDao{

	@Override
	public BadOpenLogBean getByPhone(String phone) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT mbl.`id`,mbl.`vmCode`,mbl.`customerId`,mbl.`createTime`");
		sql.append(" FROM machines_badopen_log AS mbl ");
		sql.append(" INNER JOIN tbl_customer AS tc ON tc.`id` = mbl.`customerId`");
		sql.append(" WHERE tc.`phone` = '"+phone+"'  ORDER BY mbl.`id` DESC LIMIT 1");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BadOpenLogBean log = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				log = new BadOpenLogBean();
				log.setCreateTime(rs.getTimestamp("createTime"));
				log.setCustomerId(rs.getLong("customerId"));
				log.setId(rs.getLong("id"));
				log.setVmCode(rs.getString("vmCode"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return log;
	}

}
