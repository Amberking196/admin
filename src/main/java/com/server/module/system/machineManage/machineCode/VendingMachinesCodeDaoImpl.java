package com.server.module.system.machineManage.machineCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.machineManage.machineBase.VendingMachinesBaseBean;
import com.server.module.system.machineManage.machineBase.VendingMachinesBaseDaoImpl;
import com.server.util.ReturnDataUtil;
/**
 * author name: why
 * create time: 2018-04-10 15:07:28
 */ 
@Repository
public class  VendingMachinesCodeDaoImpl extends BaseDao<VendingMachinesCodeBean> implements VendingMachinesCodeDao{


	public static Logger log = LogManager.getLogger(VendingMachinesCodeDaoImpl.class);
	
	@Override
	public VendingMachinesCodeBean getByUnique(String areaNumber, Long machinesTypeId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select areaNumber,machinesTypeId,code,id from  vending_machines_code where areaNumber='"+areaNumber+"' and machinesTypeId="+machinesTypeId+" ");		
		List<Object> plist = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while(rs.next()) {
				log.debug(rs);
				VendingMachinesCodeBean bean=new VendingMachinesCodeBean();
				bean.setAreaNumber(rs.getString(1));
				bean.setMachinesTypeId(rs.getLong("machinesTypeId"));
				bean.setCode(rs.getLong("code"));
				bean.setId(rs.getLong("id"));
				return bean;
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			this.closeConnection(rs, pst, conn);
		}
		return null;
	}
	
	public VendingMachinesCodeBean insert(VendingMachinesCodeBean bean) {
		return super.insert(bean);
	}
	
	public boolean update(VendingMachinesCodeBean bean) {
		return super.update(bean);
	}

	@Override
	public String getFactoryNumByVmCode(String vmCode) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmb.`factoryNumber` FROM vending_machines_info AS vmi");
		sql.append(" INNER JOIN vending_machines_base AS vmb ON vmi.machinesBaseId = vmb.`id`");
		sql.append(" WHERE vmi.code = '"+vmCode+"'");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String factoryNum = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				factoryNum = rs.getString("factoryNumber");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return factoryNum;
	}
}

