package com.server.module.system.machineManage.machineAccountLibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.baseManager.stateInfo.StateInfoDao;
import com.server.module.system.machineManage.machineType.MachinesTypeBean;
import com.server.module.system.machineManage.machineType.MachinesTypeDaoImpl;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * 
 * author name: why
 * create time: 2018-04-02 11:08:14
 */
@Repository
public class VendingMachineAccountLibraryDaoImpl extends BaseDao<VendingMachineAccountLibraryBean> implements VendingMachineAccountLibraryDao {


	public static Logger log = LogManager.getLogger(VendingMachineAccountLibraryDaoImpl.class);  
	
	@SuppressWarnings("unused")
	@Autowired
	 private StateInfoDao StateInfoDaoImpl;
	
	/**
	 * 售货机商品库 查询
	 */
	@SuppressWarnings("resource")
	@Override
	public ReturnDataUtil listPage(VendingMachineAccountLibraryCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select ib.name,vw.wayNumber, vmi.costPrice,vmi.price,vmi.endTime,ib.pic,ib.barCode,ib.standard,ib.unit,ib.pack,c.name,vmi.hot from vending_machines_item vmi,item_basic ib ,vending_machines_way vw,company c WHERE vmi.basicItemId=ib.id  and ib.id=vw.itemId and vmi.companyId=c.id");
		List<Object> plist = Lists.newArrayList();
		if (StringUtil.isNotBlank(condition.getContent())){
			sql.append(" and ib.name = ?");
			plist.add(condition.getContent());
		}
		if (StringUtil.isNotBlank(condition.getBarCode())){
			sql.append(" and ib.barCode = ?");
			plist.add(condition.getBarCode());
		}
		if (condition.getState()!= null){
			sql.append(" and vmi.hot = ?");
			plist.add(condition.getState());
		}
		if (StringUtil.isNotBlank(condition.getSubordinateCompanies())){
			sql.append(" and c.name = ?");
			plist.add(condition.getSubordinateCompanies());
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
			List<VendingMachineAccountLibraryBean> list = Lists.newArrayList();
			while (rs.next()) {
				VendingMachineAccountLibraryBean bean=new VendingMachineAccountLibraryBean();
				bean.setItemName(rs.getString(1));
				bean.setNumberOfHits(rs.getInt(2));
				bean.setCostPrice(rs.getDouble(3));
				bean.setPrice(rs.getDouble(4));
				bean.setEndTime(rs.getDate(5));
				bean.setPic(rs.getString(6));
				bean.setBarCode(rs.getString(7));
				bean.setStandard(rs.getString(8));
				//通过商品的unit  得到商品单位
				String nameByState = StateInfoDaoImpl.getNameByState(Long.parseLong(rs.getString(9)));
				bean.setUnit(nameByState);
				bean.setPack(rs.getString(10));
				bean.setSubordinateCompanies(rs.getString(11));
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
