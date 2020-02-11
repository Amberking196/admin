package com.server.module.system.warehouseManage.warehouseReplenish;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.baseManager.stateInfo.StateInfoBean;
import com.server.module.system.baseManager.stateInfo.StateInfoDao;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoDaoImpl;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoServiceImpl;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillBean;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;

/**
 * author name: hjc
 * create time: 2018-05-28 17:58:52
 */ 
@Repository
public class  WarehouseReplenishDaoImpl extends BaseDao<WarehouseOutputBillBean> implements WarehouseReplenishDao{
	public static Logger log = LogManager.getLogger(WarehouseInfoDaoImpl.class); 

	@Autowired
	private CompanyDao companyDaoImpl;
	@Autowired
	private StateInfoDao stateInfoDaoImpl;
	public ReturnDataUtil listPage(WarehouseReplenishForm warehouseReplenishForm) {
		log.info("<WarehouseReplenishDaoImpl>------<listPage>-----start");

		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		sql.append("select number,wi.name warehouseName,wob.type,wob.state,wbi.itemName,wbi.quantity,wob.output,si.name stateName,wob.createTime,li.name auditorName,wob.companyName");
		sql.append(" from warehouse_bill_item as wbi");
		sql.append(" left join warehouse_output_bill wob on wob.id = wbi.billId ");
		sql.append(" left join warehouse_info wi on wob.warehouseId = wi.id");
		sql.append(" left join state_info si on wob.state = si.state");
		sql.append(" left join login_info li on wob.operator = li.id ");
		sql.append(" left join vending_line vl on wob.lineId = vl.id ");
		sql.append(" left join vending_area va on wob.areaId = va.id ");
		
		sql.append(" where wob.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		
		if (warehouseReplenishForm.getStartDate() != null) {
			sql.append(" and wob.createTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(warehouseReplenishForm.getStartDate()) + "' ");
		}
		if (warehouseReplenishForm.getEndDate() != null) {
			sql.append(" and wob.createTime <= '" + DateUtil.formatLocalYYYYMMDDHHMMSS(warehouseReplenishForm.getEndDate(),1) + "' ");
		}
		if (warehouseReplenishForm.getCompanyId() != null) {
			if(warehouseReplenishForm.getAreaId() != null || warehouseReplenishForm.getLineId() != null) {
				sql.append(" and vl.companyId = "+ warehouseReplenishForm.getCompanyId());
			}else {
				sql.append(" and wob.companyId = "+ warehouseReplenishForm.getCompanyId());
			}
		}
		if (warehouseReplenishForm.getAreaId() != null) {		
			sql.append(" and va.id = "+ warehouseReplenishForm.getAreaId());
		}
		if (warehouseReplenishForm.getLineId() != null) {
			sql.append(" and vl.id = "+ warehouseReplenishForm.getLineId());
		}
		if (warehouseReplenishForm.getType() != null) {
			sql.append(" and wob.type = "+ warehouseReplenishForm.getType());
		}
		if (warehouseReplenishForm.getOutput() != null) {
			sql.append(" and wob.output = "+ warehouseReplenishForm.getOutput());
		}
//		sql.append(" and si.name !='待审核' ");
		sql.append(" order by createTime desc");

		log.info("补水量查询sql语句：》》" + sql.toString());

		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			conn=openConnection();
			pst=conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count=0;
			while(rs.next()){
				count=rs.getInt(1);
			}

			long off=(warehouseReplenishForm.getCurrentPage()-1)*warehouseReplenishForm.getPageSize();
			pst=conn.prepareStatement(sql.toString()+ " limit "+off+","+warehouseReplenishForm.getPageSize());
			if(warehouseReplenishForm.getIsShowAll()==1) {
				pst=conn.prepareStatement(sql.toString());
			}
			rs = pst.executeQuery();
			int num = 0;
			List<WarehouseOutputBillBean> list = Lists.newArrayList();
			while(rs.next()){
				num++;
				WarehouseOutputBillBean bean = new WarehouseOutputBillBean();
				bean.setNum(num);
				bean.setNumber(rs.getString("number"));
				if(StringUtils.isBlank(rs.getString("auditorName")))
					bean.setAuditorName("无");
				else
					bean.setAuditorName(rs.getString("auditorName"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setItemName(rs.getString("itemName"));
				bean.setAllNum(rs.getInt("quantity"));
				
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setTime(rs.getString("createTime").substring(0, rs.getString("createTime").length()-2));
				bean.setStateName(rs.getString("stateName"));
				//得到类型名称
				StateInfoBean stateBean = stateInfoDaoImpl.getStateInfoByState(rs.getLong("type"));
				bean.setTypeName(stateBean.getName());
				if(rs.getInt("output")==0)
					bean.setOutputName("入库");
				else if(rs.getInt("output")==1)
					bean.setOutputName("出库");
				bean.setCompanyName(rs.getString("companyName"));
				
				bean.setOutput(rs.getInt("output"));
				bean.setType(rs.getInt("type"));
				list.add(bean);
			}
			if (showSql){
				log.info(sql);
			}
			data.setCurrentPage(warehouseReplenishForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<WarehouseReplenishDaoImpl>------<listPage>-----end");
			return data;
		} 
		catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		}
		finally{
			this.closeConnection(rs, pst, conn);
		}
	}

}

