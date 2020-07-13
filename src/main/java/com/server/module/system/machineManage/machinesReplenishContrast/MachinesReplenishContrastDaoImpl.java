package com.server.module.system.machineManage.machinesReplenishContrast;

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
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * 
 * @author why
 * @date: 2019年5月14日 下午5:48:06
 */
@Repository
public class MachinesReplenishContrastDaoImpl extends BaseDao<MachinesReplenishContrastBean>
		implements MachinesReplenishContrastDao {

	private static Logger log = LogManager.getLogger(MachinesReplenishContrastDaoImpl.class);
	
	@Autowired
	private CompanyDao companyDaoImpl;

	public ReturnDataUtil listPage(MachinesReplenishContrastForm machinesReplenishContrastForm) {
		log.info("<MachinesReplenishContrastDaoImpl>------<listPage>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select m.state,m.id,m.vmCode,m.wayNumber,m.lastReplenishNum,m.thisReplenishNum,m.salesQuantity,m.thisRepertory as thisReplenishRepertory,");
		sql.append(" CASE when vmi.machineVersion=2 then   vmit.num ");
		sql.append("  when vmi.machineVersion=1 then vmw.num end    thisRepertory, ");
		sql.append(" m.createTime,m.createUser,m.updateTime,m.updateUser,m.deleteFlag,m.startTime,l.name,vmi.locatoinName,c.`name` companyName ");
		sql.append("  from machines_replenish_contrast m LEFT JOIN login_info l on m.createUser=l.id  ");
		sql.append(" LEFT JOIN vending_machines_info vmi on m.vmCode=vmi.`code` ");
		sql.append(" LEFT JOIN company c on vmi.companyId=c.id  ");
		sql.append(" left join vending_machines_way_item  vmit on m.vmCode=vmit.vmCode and m.wayNumber=vmit.wayNumber and m.orderNumber=vmit.orderNumber ");
		sql.append(" LEFT join vending_machines_way vmw on  m.vmCode=vmw.vendingMachinesCode and m.wayNumber=vmw.wayNumber where 1=1 ");
		if(StringUtil.isNotBlank( machinesReplenishContrastForm.getVmCode())) {
			sql.append(" and m.vmCode='"+machinesReplenishContrastForm.getVmCode()+"' ");
		}
		if(machinesReplenishContrastForm.getWayNumber()!=null) {
			sql.append(" and m.wayNumber='"+machinesReplenishContrastForm.getWayNumber()+"' ");
		}
		if(machinesReplenishContrastForm.getCompanyId()!=null) {
			sql.append(" and c.id in "+companyDaoImpl.findAllSonCompanyIdForInSql(machinesReplenishContrastForm.getCompanyId())+" ");
		}else {
			sql.append(" and  c.id in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+" ");
		}
		sql.append(" order by m.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("销售补货预警SQL语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (machinesReplenishContrastForm.getCurrentPage() - 1) * machinesReplenishContrastForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + machinesReplenishContrastForm.getPageSize());
			rs = pst.executeQuery();
			List<MachinesReplenishContrastBean> list = Lists.newArrayList();
			while (rs.next()) {
				MachinesReplenishContrastBean bean = new MachinesReplenishContrastBean();
				bean.setId(rs.getLong("id"));
				bean.setState(rs.getInt("state"));
				if(bean.getState()==1) {
					bean.setStateName("已解决");
				}
				bean.setVmCode(rs.getString("vmCode"));
				bean.setWayNumber(rs.getInt("wayNumber"));
				bean.setLastReplenishNum(rs.getInt("lastReplenishNum"));
				bean.setThisReplenishNum(rs.getInt("thisReplenishNum"));
				bean.setSalesQuantity(rs.getInt("salesQuantity"));
				bean.setThisRepertory(rs.getInt("thisRepertory"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setName(rs.getString("name"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setLocatoinName(rs.getString("locatoinName"));
				bean.setThisReplenishRepertory(rs.getInt("thisReplenishRepertory"));
				list.add(bean);
			}
			data.setCurrentPage(machinesReplenishContrastForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<MachinesReplenishContrastDaoImpl>------<listPage>-----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public MachinesReplenishContrastBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		MachinesReplenishContrastBean entity = new MachinesReplenishContrastBean();
		return super.del(entity);
	}

	public boolean update(MachinesReplenishContrastBean entity) {
		return super.update(entity);
	}

	public MachinesReplenishContrastBean insert(MachinesReplenishContrastBean entity) {
		return super.insert(entity);
	}

	public List<MachinesReplenishContrastBean> list(MachinesReplenishContrastForm machinesReplenishContrastForm) {
		return null;
	}
}
