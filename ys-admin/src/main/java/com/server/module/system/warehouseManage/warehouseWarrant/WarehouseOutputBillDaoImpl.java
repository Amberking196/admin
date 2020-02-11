package com.server.module.system.warehouseManage.warehouseWarrant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.baseManager.stateInfo.StateInfoBean;
import com.server.module.system.baseManager.stateInfo.StateInfoDao;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: why create time: 2018-05-16 00:05:25
 */
@Repository
public class WarehouseOutputBillDaoImpl extends BaseDao<WarehouseOutputBillBean> implements WarehouseOutputBillDao {

	public static Logger log = LogManager.getLogger(WarehouseOutputBillDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;
	@Autowired
	private StateInfoDao stateInfoDaoImpl;

	/**
	 * 入库单列表查询
	 */
	public ReturnDataUtil listPage(WarehouseOutputBillForm warehouseOutputBillForm) {
		log.info("<WarehouseWarrantInfoDaoImpl>------<listPage>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select wob.id,number,wi.name warehouseName,wob.createTime,li.name operatorName,lin.name auditorName,si.name stateName,auditor,wob.type,wob.happenDate ");
		sql.append(" from warehouse_output_bill wob  left join warehouse_info wi on wob.warehouseId=wi.id  ");
		sql.append(" left join state_info si on wob.state=si.state   left join login_info li on wob.operator=li.id   ");
		sql.append(" left join login_info lin on wob.auditor=lin.id ");
		sql.append("  where wi.companyId in "+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" and output=0 and wob.deleteFlag=0  ");
		if(warehouseOutputBillForm.getType()!=null) {
			sql.append(" and wob.type=" + warehouseOutputBillForm.getType() + " ");
		}else {
			sql.append(" and wob.type!=60205 ");
		}
		if (warehouseOutputBillForm.getState() != null) {
			sql.append(" and wob.state=" + warehouseOutputBillForm.getState() + " ");
		}
		if (warehouseOutputBillForm.getWarehouseId() != null) {
			sql.append(" and wi.id=" + warehouseOutputBillForm.getWarehouseId() + " ");
		}
		if (warehouseOutputBillForm.getStartTime() != null) {
			sql.append(" and wob.createTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(warehouseOutputBillForm.getStartTime()) + "' ");
		}
		if (warehouseOutputBillForm.getEndTime() != null) {
			sql.append(
					" and wob.createTime < '" + DateUtil.formatLocalYYYYMMDDHHMMSS(warehouseOutputBillForm.getEndTime(), 1) + "' ");
		}
		if(StringUtil.isNotBlank(warehouseOutputBillForm.getNumber())) {
			sql.append(" and wob.number=" + warehouseOutputBillForm.getNumber() + " ");
		}
		sql.append(" order by wob.state,wob.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("入库单列表查询sql语句：》》" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (warehouseOutputBillForm.getCurrentPage() - 1) * warehouseOutputBillForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + warehouseOutputBillForm.getPageSize());
			rs = pst.executeQuery();
			List<WarehouseOutputBillBean> list = Lists.newArrayList();
			int num = 0;
			while (rs.next()) {
				num++;
				WarehouseOutputBillBean bean = new WarehouseOutputBillBean();
				bean.setId(rs.getLong("id"));
				bean.setNum(num);
				bean.setNumber(rs.getString("number"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setOperatorName(rs.getString("operatorName"));
				if(rs.getInt("auditor")!=0) {
					bean.setAuditorName(rs.getString("auditorName"));
				}
			
				bean.setTime(rs.getString("createTime").substring(0, rs.getString("createTime").length()-2));
				if ( "已审核".equals(rs.getString("stateName")) || "已入库".equals(rs.getString("stateName"))) {
					bean.setIsShow(1);
				} else {
					bean.setIsShow(0);
				}
				bean.setStateName(rs.getString("stateName"));
				bean.setHappenDate(rs.getTimestamp("happenDate"));
				bean.setType(rs.getInt("type"));
				bean.setTypeLabel(bean.getTypeLabel());
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(warehouseOutputBillForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<WarehouseWarrantInfoDaoImpl>------<listPage>-----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 根据入单号 查询详情
	 */
	@Override
	public WarehouseOutputBillBean get(String changeId) {
		log.info("<WarehouseWarrantInfoDaoImpl>------<get>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select wob.id,wob.warehouseId,number,supplierId,s.companyName supplierName,wi.name warehouseName,si.name stateName,wob.createTime,wob.targetWarehouseId,wob.sourceWarehouseId,li.name operatorName,type,auditor,wob.purchaseId,lin.name auditorName,wob.happenDate from warehouse_output_bill wob ");
		sql.append(" left join warehouse_info wi on wob.warehouseId=wi.id ");
		sql.append(" left join login_info li on  wob.operator=li.id  ");
		sql.append(" left join login_info lin on  wob.auditor=lin.id  ");
		sql.append(" left join state_info si on  wob.state=si.state  ");
		sql.append(" left join supplier s on   wob.supplierId=s.id ");
		sql.append(" where  wob.number='" + changeId + "'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("根据入单号 查询详情sql语句：》》" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			WarehouseOutputBillBean bean = null;
			while (rs.next()) {
				bean = new WarehouseOutputBillBean();
				bean.setId(rs.getLong("id"));
				bean.setWarehouseId(rs.getLong("warehouseId"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setOperatorName(rs.getString("operatorName"));
				if(rs.getInt("auditor")!=0) {
					bean.setAuditorName(rs.getString("auditorName"));
				}
				bean.setType(rs.getInt("type"));
				//得到类型名称
				StateInfoBean stateBean = stateInfoDaoImpl.getStateInfoByState(rs.getLong("type"));
				bean.setTypeName(stateBean.getName());
				bean.setNumber(rs.getString("number"));
				bean.setSupplierId(rs.getInt("supplierId"));
				bean.setSupplierName(rs.getString("supplierName"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setTime(rs.getString("createTime").substring(0, rs.getString("createTime").length() - 2));
				bean.setStateName(rs.getString("stateName"));
				bean.setTargetWarehouseId(rs.getLong("targetWarehouseId"));
				bean.setSourceWarehouseId(rs.getLong("sourceWarehouseId"));
				bean.setPurchaseId(rs.getInt("purchaseId"));
				bean.setHappenDate(rs.getTimestamp("happenDate"));
			}
			log.info("<WarehouseWarrantInfoDaoImpl>------<get>-----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return null;
	}
	
	/**
	 * 根据入单号 查询详情
	 */
	@Override
	public WarehouseOutputBillBean gettransaction(Connection conn,String changeId) {
		log.info("<WarehouseWarrantInfoDaoImpl>------<get>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select wob.id,wob.warehouseId,number,supplierId,s.companyName supplierName,wi.name warehouseName,si.name stateName,wob.createTime,wob.targetWarehouseId,wob.sourceWarehouseId,li.name operatorName,type,auditor,wob.purchaseId,lin.name auditorName,wob.companyId,wob.companyName from warehouse_output_bill wob ");
		sql.append(" left join warehouse_info wi on wob.warehouseId=wi.id ");
		sql.append(" left join login_info li on  wob.operator=li.id  ");
		sql.append(" left join login_info lin on  wob.auditor=lin.id  ");
		sql.append(" left join state_info si on  wob.state=si.state  ");
		sql.append(" left join supplier s on   wob.supplierId=s.id ");
		sql.append(" where  wob.number='" + changeId + "'");
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("根据入单号 查询详情sql语句：》》" + sql.toString());
		try {
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			WarehouseOutputBillBean bean = null;
			while (rs.next()) {
				bean = new WarehouseOutputBillBean();
				bean.setId(rs.getLong("id"));
				bean.setWarehouseId(rs.getLong("warehouseId"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setOperatorName(rs.getString("operatorName"));
				if(rs.getInt("auditor")!=0) {
					bean.setAuditorName(rs.getString("auditorName"));
				}
				bean.setType(rs.getInt("type"));
				//得到类型名称
				StateInfoBean stateBean = stateInfoDaoImpl.getStateInfoByState(rs.getLong("type"));
				bean.setTypeName(stateBean.getName());
				bean.setNumber(rs.getString("number"));
				bean.setSupplierId(rs.getInt("supplierId"));
				bean.setSupplierName(rs.getString("supplierName"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setTime(rs.getString("createTime").substring(0, rs.getString("createTime").length() - 2));
				bean.setStateName(rs.getString("stateName"));
				bean.setTargetWarehouseId(rs.getLong("targetWarehouseId"));
				bean.setSourceWarehouseId(rs.getLong("sourceWarehouseId"));
				bean.setPurchaseId(rs.getInt("purchaseId"));
				bean.setCompanyId(rs.getInt("companyId"));
				bean.setCompanyName(rs.getString("companyName"));
			}
			log.info("<WarehouseWarrantInfoDaoImpl>------<get>-----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, null);
		}
		return null;
	}

	/**
	 * 生成入库单号
	 */
	public String findChangeId() {
		log.info("<WarehouseWarrantInfoDaoImpl>------<findChangeId>-----start");
		// 得到当前日期
		String time = DateUtil.formatYYYYMMDD(new Date());
		String[] split = time.split("-");
		String one = split[0] + split[1] + split[2];
		Long id = UserUtils.getUser().getId();
		String two = null;
		if (id < 10) {
			two = "00" + id;
		} else if (10 <= id && id < 100) {
			two = "0" + id;
		} else {
			two = id + "";
		}
		String changeId = one +"0"+two;
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select number from warehouse_output_bill where number like '" + changeId
				+ "%' order by createTime desc");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("生成入库单号sql语句：》》" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			String findChangeId = null;
			if (rs.next()) {
				String id1 = rs.getString("number");
				Integer valueOf = Integer.valueOf(id1.substring(12, 15));
				valueOf++;
				if (valueOf < 10) {
					findChangeId = one + "0" + two + "00" + valueOf;
				} else if (10 <= valueOf && valueOf < 100) {
					findChangeId = one + "0" + two + "0" + valueOf;
				} else {
					findChangeId = one + "0" + two + valueOf + "";
				}
			} else {
				findChangeId = one + "0" + two + "001";
			}
			log.info("<WarehouseWarrantInfoDaoImpl>------<findChangeId>-----end");
			return findChangeId;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return null;
	}

	/**
	 * 修改入库单
	 */
	public boolean update(WarehouseOutputBillBean entity) {
		return super.update(entity);
	}

	/**
	 * 增加入库单
	 */
	public WarehouseOutputBillBean insert(WarehouseOutputBillBean entity) {
		return super.insert(entity);
	}
	/**
	 * 事务实现
	 *@author why
	 *@date 2018年9月11日-上午9:42:43
	 *@param conn
	 *@param entity
	 *@return
	 */
	public WarehouseOutputBillBean insertTransaction(Connection conn,WarehouseOutputBillBean entity) {
		return (WarehouseOutputBillBean) super.insert(conn,entity);
	}
	
	public WarehouseOutputBillBean getById(Object id) {
		

		return super.get(id);
	}

	/**
	 * 事务控制
	 *@author why
	 *@date 2018年9月11日-上午11:20:48
	 *@param conn
	 *@param id
	 *@return
	 */
	public WarehouseOutputBillBean getById(Connection conn,Object id) {
		return super.get(id);
	}

	public boolean update(Connection conn,WarehouseOutputBillBean entity) {
		return super.update(conn,entity);
	}

	/**
	 * 库存转移时 查询已经入库数量
	 */
	@Override
	public Long getQuantity(Long removalStockId ,Long targetWarehouseId, Long sourceWarehouseId, Long itemId) {
		log.info("<WarehouseWarrantInfoDaoImpl>------<getQuantity>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(quantity) quantity from warehouse_bill_item where billId in ( select id from warehouse_output_bill ");
		sql.append(" where type=60206 and state=60203 and purchaseId="+removalStockId+" and targetWarehouseId="+targetWarehouseId+" and sourceWarehouseId="+sourceWarehouseId+" ) ");
		sql.append(" and itemId="+itemId+" ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("库存转移时 查询已经入库数量sql语句：》》" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			WarehouseOutputBillBean bean = null;
			while (rs.next()) {
				log.info("<WarehouseWarrantInfoDaoImpl>------<getQuantity>-----end");
				return rs.getLong("quantity");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<WarehouseWarrantInfoDaoImpl>------<getQuantity>-----end");
		return null;
	}



}
