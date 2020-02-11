package com.server.module.system.warehouseManage.warehouseRemoval;

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
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.baseManager.stateInfo.StateInfoBean;
import com.server.module.system.baseManager.stateInfo.StateInfoDao;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.warehouseManage.stock.WarehouseStockBean;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillBean;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: why create time: 2018-05-16 00:05:25
 */
@Repository
public class WarehouseRemovalDaoImpl extends BaseDao<WarehouseOutputBillBean> implements WarehouseRemovalDao {

	public static Logger log = LogManager.getLogger(WarehouseRemovalDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;
	@Autowired
	private StateInfoDao stateInfoDaoImpl;

	/**
	 * 出库单列表查询
	 */
	public ReturnDataUtil listPage(WarehouseRemovalForm warehouseRemovalForm) {
		log.info("<WarehouseRemovalDaoImpl>------<listPage>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select wob.id,wob.warehouseId,number,wi.name warehouseName,si.name stateName, wob.createTime,type,consigneeId,consigneeName,li.name operatorName,wob.type,wob.auditor,lin.name auditorName,wob.happenDate ");
		sql.append(" from warehouse_output_bill wob  left join warehouse_info wi on wob.warehouseId=wi.id  ");
		sql.append(" left join state_info si on wob.state=si.state   left join login_info li on wob.operator=li.id  left join login_info lin on wob.auditor=lin.id ");
		sql.append(" where wi.companyId in  "
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()) + " and output=1  and wob.deleteFlag=0 ");
		if (warehouseRemovalForm.getState() != null) {
			sql.append(" and wob.state=" + warehouseRemovalForm.getState() + " ");
		}
		if(warehouseRemovalForm.getType()!=null) {
			sql.append(" and wob.type=" + warehouseRemovalForm.getType() + " ");
		}
		if (warehouseRemovalForm.getWarehouseId() != null) {
			sql.append(" and wi.id=" + warehouseRemovalForm.getWarehouseId() + " ");
		}
		if (warehouseRemovalForm.getStartTime() != null) {
			sql.append(" and wob.createTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(warehouseRemovalForm.getStartTime())
					+ "' ");
		}
		if (warehouseRemovalForm.getEndTime() != null) {
			sql.append(" and wob.createTime < '"
					+ DateUtil.formatLocalYYYYMMDDHHMMSS(warehouseRemovalForm.getEndTime(), 1) + "' ");
		}
		if (StringUtil.isNotBlank(warehouseRemovalForm.getNumber())) {
			sql.append(" and wob.number=" + warehouseRemovalForm.getNumber() + " ");
		}
		sql.append(" order by wob.state,wob.createTime desc  ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("出库单列表查询sql语句：》》" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (warehouseRemovalForm.getCurrentPage() - 1) * warehouseRemovalForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + warehouseRemovalForm.getPageSize());
			rs = pst.executeQuery();
			List<WarehouseOutputBillBean> list = Lists.newArrayList();
			int num = 0;
			while (rs.next()) {
				num++;
				WarehouseOutputBillBean bean = new WarehouseOutputBillBean();
				bean.setId(rs.getLong("id"));
				bean.setNum(num);
				bean.setNumber(rs.getString("number"));
				bean.setWarehouseId(rs.getLong("warehouseId"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setStateName(rs.getString("stateName"));
				bean.setCreateTime(rs.getDate("createTime"));
				if (rs.getString("createTime") != null) {
					bean.setTime(rs.getString("createTime").substring(0, rs.getString("createTime").length() - 2));
				}
				// 得到类型名称
				StateInfoBean stateBean = stateInfoDaoImpl.getStateInfoByState(rs.getLong("type"));
				if (stateBean != null) {
					bean.setTypeName(stateBean.getName());
				}
				bean.setConsigneeId(rs.getInt("consigneeId"));
				bean.setConsigneeName(rs.getString("consigneeName"));
				bean.setOperatorName(rs.getString("operatorName"));
				if(rs.getInt("auditor")!=0) {
					bean.setAuditorName(rs.getString("auditorName"));
				}
				if ("已审核".equals(rs.getString("stateName")) || "已出库".equals(rs.getString("stateName"))
						|| "已撤销".equals(rs.getString("stateName"))) {
					bean.setIsShow(1);
				} else {
					bean.setIsShow(0);
				}
				bean.setHappenDate(rs.getTimestamp("happenDate"));
				bean.setType(rs.getInt("type"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(warehouseRemovalForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<WarehouseRemovalDaoImpl>------<listPage>-----end");
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
	 * 根据 出单号 查询详情
	 */
	@Override
	public WarehouseOutputBillBean get(String changeId) {
		log.info("<WarehouseRemovalDaoImpl>------<get>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select wob.id,wob.warehouseId,number,wob.state,supplierId,s.companyName supplierName,wi.name warehouseName,si.name stateName,wob.createTime,li.name operatorName,type,consigneeId,consigneeName,wob.companyName,wob.lineId,wob.areaId,wob.remark,wob.targetWarehouseId,wob.sourceWarehouseId,wob.auditor,lin.name auditorName,wob.happenDate  from warehouse_output_bill wob ");
		sql.append(" left join warehouse_info wi on wob.warehouseId=wi.id ");
		sql.append(" left join login_info li on  wob.operator=li.id  ");
		sql.append(" left join login_info lin on  wob.auditor=lin.id ");
		sql.append(" left join state_info si on  wob.state=si.state  ");
		sql.append(" left join supplier s on   wob.supplierId=s.id ");
		sql.append(" where  wob.number='" + changeId + "'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("根据出单号查询详情sql语句：》》" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			WarehouseOutputBillBean bean = null;
			while (rs.next()) {
				bean = new WarehouseOutputBillBean();
				bean.setId(rs.getLong("id"));
				bean.setWarehouseId(rs.getLong("warehouseId"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setOperatorName(rs.getString("operatorName"));
				if(rs.getInt("auditor")!=0) {
					bean.setAuditorName(rs.getString("auditorName"));
				}
				bean.setType(rs.getInt("type"));
				String typeName = stateInfoDaoImpl.getNameByState(rs.getLong("type"));
				if (typeName != null) {
					bean.setTypeName(typeName);
				}
				bean.setNumber(rs.getString("number"));
				bean.setSupplierId(rs.getInt("supplierId"));
				bean.setSupplierName(rs.getString("supplierName"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setTime(rs.getString("createTime").substring(0, rs.getString("createTime").length() - 2));
				bean.setStateName(rs.getString("stateName"));
				if ("已审核".equals(rs.getString("stateName")) || "已出库".equals(rs.getString("stateName"))) {
					bean.setIsShow(0);
				} else {
					bean.setIsShow(1);
				}
				bean.setState(rs.getInt("state"));
				bean.setConsigneeId(rs.getInt("consigneeId"));
				bean.setConsigneeName(rs.getString("consigneeName"));
				bean.setLineId(rs.getInt("lineId"));
				bean.setAreaId(rs.getInt("areaId"));
				bean.setRemark(rs.getString("remark"));
				bean.setTargetWarehouseId(rs.getLong("targetWarehouseId"));
				bean.setSourceWarehouseId(rs.getLong("sourceWarehouseId"));
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
	 * 生成出库单号
	 */
	public String findChangeId() {
		log.info("<WarehouseRemovalDaoImpl>------<findChangeId>-----start");
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
		String changeId = one + "1" + two;
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select number from warehouse_output_bill where number like '" + changeId
				+ "%' order by createTime desc");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("生成出库单号sql语句：》》" + sql.toString());
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
					findChangeId = one + "1" + two + "00" + valueOf;
				} else if (10 <= valueOf && valueOf < 100) {
					findChangeId = one + "1" + two + "0" + valueOf;
				} else {
					findChangeId = one + "1" + two + valueOf + "";
				}
			} else {
				findChangeId = one + "1" + two + "001";
			}
			log.info("<WarehouseRemovalDaoImpl>------<findChangeId>-----end");
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
	 * 修改出库单
	 */
	public boolean update(WarehouseOutputBillBean entity) {
		return super.update(entity);
	}

	/**
	 * 增加出库单
	 */
	public WarehouseOutputBillBean insert(WarehouseOutputBillBean entity) {
		return super.insert(entity);
	}

	/**
	 * 判断库存是否足够
	 */
	@Override
	public List<WarehouseStockBean> checkQuantity(Long warehouseId ,String itemIds) {
		log.info("<WarehouseRemovalDaoImpl>------<checkQuantity>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append("select itemId,quantity,costPrice from warehouse_stock where warehouseId='"+warehouseId+"' and itemId in ("+itemIds+") ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("判断库存sql语句：》》" + sql.toString());
		List<WarehouseStockBean> list=Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				WarehouseStockBean bean=new WarehouseStockBean();
				bean.setItemId(rs.getLong("itemId"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCostPrice(rs.getDouble("costPrice"));
				list.add(bean);
			}
			log.info("<WarehouseWarrantInfoDaoImpl>------<checkQuantity>-----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return null;
	}

	@Override
	public WarehouseOutputBillBean getBean(Object id) {
		return super.get(id);
	}

	
}
