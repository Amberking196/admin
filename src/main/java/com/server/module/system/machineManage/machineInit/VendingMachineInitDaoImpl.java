package com.server.module.system.machineManage.machineInit;

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
import com.server.module.system.machineManage.machineList.MachinesInfoAndBaseDto;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoDaoImpl;
import com.server.util.DateUtil;
import com.server.util.DateUtils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * 
 * author name: why
 * create time: 2018-04-03 09:54:06
 */
@Repository
public class VendingMachineInitDaoImpl extends BaseDao<VendingMachineInitBean> implements VendingMachineInitDao{

	public static Logger log = LogManager.getLogger(VendingMachineInitDaoImpl.class); 	
	/**
	 * 查询售货机初始化
	 */
	@SuppressWarnings("resource")
	@Override
	public ReturnDataUtil listPage(VendingMachineInitCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql=new StringBuilder("select id,vendingMachinesCode,startDate,endDate,isInit  from vending_machine_init vi where 1=1 ");
		   
	        List<Object> plist=Lists.newArrayList();
	        if (StringUtil.isNotEmpty(condition.getVendingMachinesCode())){
	        	sql.append(" and vi.vendingMachinesCode=? ");
	        	plist.add(condition.getVendingMachinesCode());
	        }
	        if (condition.getStartDate()!=null){
	        	sql.append(" and vi.startDate >= '"+DateUtil.formatYYYYMMDD(condition.getStartDate())+"'");
	        	plist.add(condition.getStartDate());
	        }
	        if (condition.getEndDate()!= null ){
	        	sql.append(" and vi.startDate <= '"+DateUtil.formatYYYYMMDD(condition.getEndDate())+"'");
	        	plist.add(condition.getEndDate());
	        }
	        
	        if (condition.getState()!=null){
	        	sql.append(" and vi.isInit=? ");
	        	plist.add(condition.getState());
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
			List<VendingMachineInitBean> list = Lists.newArrayList();
			while (rs.next()) {
				VendingMachineInitBean	 bean = new VendingMachineInitBean();
				bean.setId(rs.getInt(1));
				bean.setVendingMachinesCode(rs.getString(2));
				bean.setStartDate(rs.getDate(3));
				bean.setEndDate(rs.getDate(4));
				bean.setIsInit(rs.getInt(5));
				if(rs.getDate(4)!=null) {
					Long timeSecond = DateUtils.getTimeSecond(rs.getDate(3), rs.getDate(4));
					bean.setConsumeTime(timeSecond+"秒");
				}
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
