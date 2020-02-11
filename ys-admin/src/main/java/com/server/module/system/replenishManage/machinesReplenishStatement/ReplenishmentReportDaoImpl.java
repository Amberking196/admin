package com.server.module.system.replenishManage.machinesReplenishStatement;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.replenishManage.machinesReplenishManage.MachinesReplenishBean;
import com.server.module.system.replenishManage.machinesReplenishManage.MachinesReplenishDao;
import com.server.module.system.replenishManage.machinesReplenishManage.ReplenishmentDetailsBean;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;

/**
 * author name: why create time: 2018-04-24 11:53:01
 */
@Repository
public class ReplenishmentReportDaoImpl extends BaseDao<ReplenishmentReportBean> implements ReplenishmentReportDao {

	public static Logger log = LogManager.getLogger(ReplenishmentReportDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;

	/**
	 * 补货报表列表查询
	 */
	@SuppressWarnings("resource")
	public ReturnDataUtil listPage(ReplenishmentReportForm replenishmentReportForm) {
		log.info("<ReplenishmentReportDaoImpl>----<listPage>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select * from ( select c.name companyName,vl.name lineName,vmCode,vwl.wayNumber,i.name itemName,vwl.adjustNum,preNum,num,(num-preNum) quantity,vwl.createTime,li.name userName ");
		sql.append(" from  replenish_record ");
		sql.append(" vwl left join  vending_machines_info  vmi on vwl.vmCode=vmi.code ");
		sql.append(" left join vending_line vl on vmi.lineId=vl.id left join company c on vmi.companyId=c.id    ");
		sql.append(" left join login_info li on vwl.userId=li.id left join item_basic i on vwl.basicItemId=i.id");
		sql.append(" where vwl.opType=2 and  vmi.companyId in"
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		//sql.append(" or  FIND_IN_SET( vmi.code,(select GROUP_CONCAT(vmCode) from login_info_machine lim where lim.way=vwl.wayNumber and lim.userId="+UserUtils.getUser().getId()+")))");

		if (replenishmentReportForm.getAreaId() != null && replenishmentReportForm.getAreaId()>0) {
			sql.append(" and vmi.areaId = '" + replenishmentReportForm.getAreaId() + "' ");
		}
		if (replenishmentReportForm.getUserId() != null) {
			sql.append(" and li.id = '" + replenishmentReportForm.getUserId() + "' ");
		}
		if (replenishmentReportForm.getCompanyId() != null) {
			sql.append(" and c.id = '" + replenishmentReportForm.getCompanyId() + "' ");
		}
//		if (replenishmentReportForm.getAreaId() != null) {
//			sql.append("and vl.areaId = '" + replenishmentReportForm.getAreaId() + "' ");
//		}
		if (replenishmentReportForm.getLineId() != null) {
			sql.append(" and vl.id = '" + replenishmentReportForm.getLineId() + "' ");
		}
		if (StringUtil.isNotEmpty(replenishmentReportForm.getCode())) {
			sql.append(" and vwl.vmcode like '%" + replenishmentReportForm.getCode() + "%' ");
		}
		if (StringUtil.isNotEmpty(replenishmentReportForm.getOperator())) {
			sql.append(" and li.name like '%" + replenishmentReportForm.getOperator() + "%' ");
		}
		if (replenishmentReportForm.getStartDate() != null) {
			sql.append(" and vwl.createTime >= '"
					+ DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getStartDate()) + "' ");
		}
		if (replenishmentReportForm.getEndDate() != null) {
			sql.append(" and vwl.createTime < '"
					+ DateUtil.formatLocalYYYYMMDDHHMMSS(replenishmentReportForm.getEndDate(), 1) + "' ");
		}
		sql.append(" order by vwl.createTime desc , vwl.vmCode asc ) A union ");

		sql.append(
				" ( select c.name companyName,vl.name lineName,vwl.vmCode,vwl.wayNumber,i.name itemName,vwl.adjustNum,preNum,num,(num-preNum) quantity,vwl.createTime,li.name userName ");
		sql.append(" from  replenish_record ");
		sql.append(" vwl left join  vending_machines_info  vmi on vwl.vmCode=vmi.code ");
		sql.append(" left join vending_line vl on vmi.lineId=vl.id left join company c on vmi.companyId=c.id    ");
		sql.append(" left join login_info li on vwl.userId=li.id left join item_basic i on vwl.basicItemId=i.id ");
		sql.append(" left  join login_info_machine lim on lim.way = vwl.wayNumber and lim.vmCode = vwl.vmCode ");

		sql.append(" where vwl.opType=2 and ");
		sql.append("  lim.userId="+UserUtils.getUser().getId()+"");

		if (replenishmentReportForm.getAreaId() != null && replenishmentReportForm.getAreaId()>0) {
			sql.append(" and vmi.areaId = '" + replenishmentReportForm.getAreaId() + "' ");
		}
		if (replenishmentReportForm.getUserId() != null) {
			sql.append(" and li.id = '" + replenishmentReportForm.getUserId() + "' ");
		}
		if (replenishmentReportForm.getCompanyId() != null) {
			sql.append(" and c.id = '" + replenishmentReportForm.getCompanyId() + "' ");
		}
//		if (replenishmentReportForm.getAreaId() != null) {
//			sql.append("and vl.areaId = '" + replenishmentReportForm.getAreaId() + "' ");
//		}
		if (replenishmentReportForm.getLineId() != null) {
			sql.append(" and vl.id = '" + replenishmentReportForm.getLineId() + "' ");
		}
		if (StringUtil.isNotEmpty(replenishmentReportForm.getCode())) {
			sql.append(" and vwl.vmcode like '%" + replenishmentReportForm.getCode() + "%' ");
		}
		if (StringUtil.isNotEmpty(replenishmentReportForm.getOperator())) {
			sql.append(" and li.name like '%" + replenishmentReportForm.getOperator() + "%' ");
		}
		if (replenishmentReportForm.getStartDate() != null) {
			sql.append(" and vwl.createTime >= '"
					+ DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getStartDate()) + "' ");
		}
		if (replenishmentReportForm.getEndDate() != null) {
			sql.append(" and vwl.createTime < '"
					+ DateUtil.formatLocalYYYYMMDDHHMMSS(replenishmentReportForm.getEndDate(), 1) + "' ");
		}

		sql.append(" order by vwl.createTime desc , vwl.vmCode asc ) ");

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("补货报表sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement("select count(*) from("+sql.toString()+")A");
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (replenishmentReportForm.getCurrentPage() - 1) * replenishmentReportForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + replenishmentReportForm.getPageSize());

			rs = pst.executeQuery();
			List<ReplenishmentReportBean> list = Lists.newArrayList();
			int id = 0;
			while (rs.next()) {
				id++;
				ReplenishmentReportBean bean = new ReplenishmentReportBean();
				bean.setId(id);
				bean.setCompanyName(rs.getString("companyName"));
				bean.setLineName(rs.getString("lineName"));
				bean.setCode(rs.getString("vmCode"));
				bean.setWayNumber(rs.getInt("wayNumber"));
				bean.setItemName(rs.getString("itemName"));
				bean.setQuantityBeforeReplenishment(rs.getInt("preNum"));
				bean.setQuantityAfterReplenishment(rs.getInt("num"));
				bean.setQuantityReplenishment(rs.getInt("quantity"));
				String time = rs.getString("createTime");
				time = time.substring(0, time.length() - 2);
				bean.setReplenishmentTime(time);
				bean.setReplenisher(rs.getString("userName"));
				
				if(rs.getBigDecimal("adjustNum")!=null)
				   bean.setAdjustNum(rs.getInt("adjustNum"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(replenishmentReportForm.getCurrentPage());
			data.setPageSize(replenishmentReportForm.getPageSize());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ReplenishmentReportDaoImpl>----<listPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public ReturnDataUtil visionListPage(ReplenishmentReportForm replenishmentReportForm) {
		log.info("<ReplenishmentReportDaoImpl>----<visionListPage>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select c.name companyName,vl.name lineName,vmCode,vwl.wayNumber,i.name itemName,vwl.adjustNum,preNum,num,(num-preNum) quantity,vwl.createTime,li.name userName,operateHistoryId ");
		sql.append(" from  replenish_record_vision ");
		sql.append(" vwl left join  vending_machines_info  vmi on vwl.vmCode=vmi.code ");
		sql.append(" left join vending_line vl on vmi.lineId=vl.id left join company c on vmi.companyId=c.id    ");
		sql.append(" left join login_info li on vwl.userId=li.id left join item_basic i on vwl.basicItemId=i.id");
		sql.append(" where vwl.opType=2 and  vmi.companyId in"
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		if (replenishmentReportForm.getUserId() != null) {
			sql.append("and li.id = '" + replenishmentReportForm.getUserId() + "' ");
		}
		if (replenishmentReportForm.getCompanyId() != null) {
			sql.append("and c.id = '" + replenishmentReportForm.getCompanyId() + "' ");
		}
		if (replenishmentReportForm.getAreaId() != null) {
			sql.append("and vl.areaId = '" + replenishmentReportForm.getAreaId() + "' ");
		}
		if (replenishmentReportForm.getLineId() != null) {
			sql.append("and vl.id = '" + replenishmentReportForm.getLineId() + "' ");
		}
		if (StringUtil.isNotEmpty(replenishmentReportForm.getCode())) {
			sql.append("and vwl.vmcode like '%" + replenishmentReportForm.getCode() + "%' ");
		}
		if (StringUtil.isNotEmpty(replenishmentReportForm.getOperator())) {
			sql.append("and li.name like '%" + replenishmentReportForm.getOperator() + "%' ");
		}
		if (replenishmentReportForm.getStartDate() != null) {
			sql.append(" and vwl.createTime >= '"
					+ DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getStartDate()) + "' ");
		}
		if (replenishmentReportForm.getEndDate() != null) {
			sql.append(" and vwl.createTime < '"
					+ DateUtil.formatLocalYYYYMMDDHHMMSS(replenishmentReportForm.getEndDate(), 1) + "' ");
		}
		sql.append(" order by vwl.createTime desc , vwl.vmCode asc  ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("补货报表sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (replenishmentReportForm.getCurrentPage() - 1) * replenishmentReportForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + replenishmentReportForm.getPageSize());
			rs = pst.executeQuery();
			List<ReplenishmentReportBean> list = Lists.newArrayList();
			int id = 0;
			while (rs.next()) {
				id++;
				ReplenishmentReportBean bean = new ReplenishmentReportBean();
				bean.setId(id);
				bean.setCompanyName(rs.getString("companyName"));
				bean.setLineName(rs.getString("lineName"));
				bean.setCode(rs.getString("vmCode"));
				bean.setWayNumber(rs.getInt("wayNumber"));
				bean.setItemName(rs.getString("itemName"));
				bean.setQuantityBeforeReplenishment(rs.getInt("preNum"));
				bean.setQuantityAfterReplenishment(rs.getInt("num"));
				bean.setQuantityReplenishment(rs.getInt("quantity"));
				String time = rs.getString("createTime");
				time = time.substring(0, time.length() - 2);
				bean.setReplenishmentTime(time);
				bean.setReplenisher(rs.getString("userName"));
				bean.setOperateHistoryId(rs.getLong("operateHistoryId"));
				if(rs.getBigDecimal("adjustNum")!=null)
				   bean.setAdjustNum(rs.getInt("adjustNum"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(replenishmentReportForm.getCurrentPage());
			data.setPageSize(replenishmentReportForm.getPageSize());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ReplenishmentReportDaoImpl>----<visionListPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	
	
	public ReturnDataUtil changeListPage(ReplenishmentReportForm replenishmentReportForm) {
		log.info("<ReplenishmentReportDaoImpl>----<listPage>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select c.name companyName,vl.name lineName,vmCode,vwl.wayNumber,i.name itemName,vwl.adjustNum,preNum,num,(num-preNum) quantity,vwl.createTime,li.name userName ");
		sql.append(" from  replenish_record  vwl left join  vending_machines_info  vmi on vwl.vmCode=vmi.code  ");
		sql.append(" left join vending_line vl on vmi.lineId=vl.id left join company c on vmi.companyId=c.id    ");
		sql.append(" left join login_info li on vwl.userId=li.id left join item_basic i on vwl.basicItemId=i.id");
		sql.append(" where vwl.opType=5 and  (vmi.companyId in"
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or FIND_IN_SET(vmi.code,(select GROUP_CONCAT(vmCode) from login_info_machine lim where lim.way=vwl.wayNumber and lim.userId="+UserUtils.getUser().getId()+")))");

		if (replenishmentReportForm.getUserId() != null) {
			sql.append("and li.id = '" + replenishmentReportForm.getUserId() + "' ");
		}
		if (replenishmentReportForm.getCompanyId() != null) {
			sql.append("and c.id = '" + replenishmentReportForm.getCompanyId() + "' ");
		}
		if (replenishmentReportForm.getAreaId() != null && replenishmentReportForm.getAreaId()>0) {
			sql.append("and vl.areaId = '" + replenishmentReportForm.getAreaId() + "' ");
		}
		if (replenishmentReportForm.getLineId() != null) {
			sql.append("and vl.id = '" + replenishmentReportForm.getLineId() + "' ");
		}
		if (StringUtil.isNotEmpty(replenishmentReportForm.getCode())) {
			sql.append("and vwl.vmcode like '%" + replenishmentReportForm.getCode() + "%' ");
		}
		if (StringUtil.isNotEmpty(replenishmentReportForm.getOperator())) {
			sql.append("and li.name like '%" + replenishmentReportForm.getOperator() + "%' ");
		}
		if (replenishmentReportForm.getStartDate() != null) {
			sql.append(" and vwl.createTime >= '"
					+ DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getStartDate()) + "' ");
		}
		if (replenishmentReportForm.getEndDate() != null) {
			sql.append(" and vwl.createTime < '"
					+ DateUtil.formatLocalYYYYMMDDHHMMSS(replenishmentReportForm.getEndDate(), 1) + "' ");
		}
		sql.append(" order by vwl.createTime desc , vwl.vmCode asc  ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("补货报表sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (replenishmentReportForm.getCurrentPage() - 1) * replenishmentReportForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + replenishmentReportForm.getPageSize());

			rs = pst.executeQuery();
			List<ReplenishmentReportBean> list = Lists.newArrayList();
			int id = 0;
			while (rs.next()) {
				id++;
				ReplenishmentReportBean bean = new ReplenishmentReportBean();
				bean.setId(id);
				bean.setCompanyName(rs.getString("companyName"));
				bean.setLineName(rs.getString("lineName"));
				bean.setCode(rs.getString("vmCode"));
				bean.setWayNumber(rs.getInt("wayNumber"));
				bean.setItemName(rs.getString("itemName"));
				bean.setQuantityBeforeReplenishment(rs.getInt("preNum"));
				bean.setQuantityAfterReplenishment(rs.getInt("num"));
				bean.setQuantityReplenishment(rs.getInt("quantity"));
				String time = rs.getString("createTime");
				time = time.substring(0, time.length() - 2);
				bean.setReplenishmentTime(time);
				bean.setReplenisher(rs.getString("userName"));
				
				if(rs.getBigDecimal("adjustNum")!=null)
				   bean.setAdjustNum(rs.getInt("adjustNum"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(replenishmentReportForm.getCurrentPage());
			data.setPageSize(replenishmentReportForm.getPageSize());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ReplenishmentReportDaoImpl>----<listPage>----end");
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
	 * 补货人员列表查询
	 */
	@SuppressWarnings("resource")
	public ReturnDataUtil userListPage(ReplenishmentReportForm replenishmentReportForm) {
		log.info("<ReplenishmentReportDaoImpl>----<userListPage>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select c.name companyName,sum((num-preNum)) as quantity,li.name userName,li.id userId ");
		sql.append(" from  replenish_record  vwl     ");
		sql.append(" inner join login_info li on vwl.userId=li.id inner join item_basic i on vwl.basicItemId=i.id");
		sql.append(" inner join vending_machines_info vmi on vmi.code=vwl.vmCode");
		sql.append(" inner join company c on  li.companyId=c.id    ");
		sql.append(" where vwl.opType=2 and  li.companyId in"
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		if (replenishmentReportForm.getCompanyId() != null) {
			sql.append("and c.id = '" + replenishmentReportForm.getCompanyId() + "' ");
		}
		if (replenishmentReportForm.getAreaId() != null && replenishmentReportForm.getAreaId()>0) {
			sql.append("and vmi.areaId = '" + replenishmentReportForm.getAreaId() + "' ");
		}
		if (replenishmentReportForm.getLineId() != null) {
			sql.append("and vl.id = '" + replenishmentReportForm.getLineId() + "' ");
		}
		if (StringUtil.isNotEmpty(replenishmentReportForm.getCode())) {
			sql.append("and vwl.vmcode like '%" + replenishmentReportForm.getCode() + "%' ");
		}
		if (StringUtil.isNotEmpty(replenishmentReportForm.getOperator())) {
			sql.append("and li.name like '%" + replenishmentReportForm.getOperator() + "%' ");
		}
		if (replenishmentReportForm.getStartDate() != null) {
			sql.append(" and vwl.createTime >= '"
					+ DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getStartDate()) + "' ");
		}
		if (replenishmentReportForm.getEndDate() != null) {
			sql.append(" and vwl.createTime < '"
					+ DateUtil.formatLocalYYYYMMDDHHMMSS(replenishmentReportForm.getEndDate(), 1) + "' ");
		}
		sql.append(" GROUP by userId order by quantity desc  ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("补货报表sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countGroupSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (replenishmentReportForm.getCurrentPage() - 1) * replenishmentReportForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + replenishmentReportForm.getPageSize());

			rs = pst.executeQuery();
			List<ReplenishmentReportBean> list = Lists.newArrayList();
			int id = 0;
			while (rs.next()) {
				id++;
				ReplenishmentReportBean bean = new ReplenishmentReportBean();
				bean.setId(id);
				bean.setCompanyName(rs.getString("companyName"));
				bean.setQuantityReplenishment(rs.getInt("quantity"));
				bean.setReplenisher(rs.getString("userName"));
				bean.setReplenisherId(rs.getInt("userId"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(replenishmentReportForm.getCurrentPage());
			data.setPageSize(replenishmentReportForm.getPageSize());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ReplenishmentReportDaoImpl>----<userListPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}

	}

	public ReturnDataUtil userlistPageDetail(ReplenishmentReportForm replenishmentReportForm) {
		log.info("<ReplenishmentReportDaoImpl>----<userListPageDetail>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select c.name companyName,vl.name lineName,vmCode,vwl.wayNumber,i.name itemName,preNum,num,(num-preNum) quantity,vwl.createTime,li.name userName ");
		sql.append(" from  replenish_record  vwl left join  vending_machines_info  vmi on vwl.vmCode=vmi.code  ");
		sql.append(" left join vending_line vl on vmi.lineId=vl.id left join company c on vmi.companyId=c.id    ");
		sql.append(" left join login_info li on vwl.userId=li.id left join item_basic i on vwl.basicItemId=i.id");
		sql.append(" where vwl.opType=2 and  li.companyId in"
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		if (replenishmentReportForm.getUserId() != null) {
			sql.append("and li.id = '" + replenishmentReportForm.getUserId() + "' ");
		}
		if (replenishmentReportForm.getCompanyId() != null) {
			sql.append("and c.id = '" + replenishmentReportForm.getCompanyId() + "' ");
		}
		if (replenishmentReportForm.getAreaId() != null) {
			sql.append("and vl.areaId = '" + replenishmentReportForm.getAreaId() + "' ");
		}
		if (replenishmentReportForm.getLineId() != null) {
			sql.append("and vl.id = '" + replenishmentReportForm.getLineId() + "' ");
		}
		if (StringUtil.isNotEmpty(replenishmentReportForm.getCode())) {
			sql.append("and vwl.vmcode like '%" + replenishmentReportForm.getCode() + "%' ");
		}
		if (StringUtil.isNotEmpty(replenishmentReportForm.getOperator())) {
			sql.append("and li.name like '%" + replenishmentReportForm.getOperator() + "%' ");
		}
		if (replenishmentReportForm.getStartDate() != null) {
			sql.append(" and vwl.createTime >= '"
					+ DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getStartDate()) + "' ");
		}
		if (replenishmentReportForm.getEndDate() != null) {
			sql.append(" and vwl.createTime < '"
					+ DateUtil.formatLocalYYYYMMDDHHMMSS(replenishmentReportForm.getEndDate(), 1) + "' ");
		}
		sql.append(" order by vwl.createTime desc , vwl.vmCode asc  ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("补货报表sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (replenishmentReportForm.getCurrentPage() - 1) * replenishmentReportForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + replenishmentReportForm.getPageSize());

			rs = pst.executeQuery();
			List<ReplenishmentReportBean> list = Lists.newArrayList();
			int id = 0;
			while (rs.next()) {
				id++;
				ReplenishmentReportBean bean = new ReplenishmentReportBean();
				bean.setId(id);
				bean.setCompanyName(rs.getString("companyName"));
				bean.setLineName(rs.getString("lineName"));
				bean.setCode(rs.getString("vmCode"));
				bean.setWayNumber(rs.getInt("wayNumber"));
				bean.setItemName(rs.getString("itemName"));
				bean.setQuantityBeforeReplenishment(rs.getInt("preNum"));
				bean.setQuantityAfterReplenishment(rs.getInt("num"));
				bean.setQuantityReplenishment(rs.getInt("quantity"));
				String time = rs.getString("createTime");
				time = time.substring(0, time.length() - 2);
				bean.setReplenishmentTime(time);
				bean.setReplenisher(rs.getString("userName"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(replenishmentReportForm.getCurrentPage());
			data.setPageSize(replenishmentReportForm.getPageSize());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ReplenishmentReportDaoImpl>----<userListPageDetail>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}

	}
}
