package com.server.module.system.machineManage.machineTemperature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.machineManage.machineList.MachinesInfoAndBaseDto;
import com.server.module.system.machineManage.machineType.MachinesTypeBean;
import com.server.redis.RedisClient;
import com.server.redis.RedisUtils;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.MainboardTypeEnum;

@Repository
public class MachineTemperatureDaoImpl extends BaseDao<MachineTemperatureBean> implements MachineTemperatureDao {

	public static Logger log=LogManager.getLogger(MachineTemperatureDaoImpl.class);
	
	@Autowired
	private CompanyDao companyDaoImpl;
	
	@Autowired 
	private RedisClient redisClient;
	/**
	 * 查询所有售货机的温度
	 */
	@Override
	public ReturnDataUtil listPage(MachineTemperatureForm machineTemperatureForm) {
		log.info("<MachineTemperatureDaoImpl>----<listPage>----start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select machineType,v.id as versionId,vmb.id,vmi.code,factoryNumber,vmi.linkMan,vmb.mainProgramVersion,vmb.canOnlineUpdate,vmi.`locatoinName` from vending_machines_base vmb inner join vending_machines_info vmi on vmb.id=vmi.machinesBaseId ");
		sql.append(" left join version_manager v on v.versionInfo = vmb.mainProgramVersion  where vmi.state=20001 and vmi.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		if(StringUtil.isNotBlank(machineTemperatureForm.getVmCode())) {
			sql.append(" and vmi.code='"+machineTemperatureForm.getVmCode()+"' ");
		}
		if(StringUtils.isNotBlank(machineTemperatureForm.getVersionInfo())){
			sql.append(" and vmb.mainProgramVersion like '%"+machineTemperatureForm.getVersionInfo()+"%'");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("售货机的温度sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (machineTemperatureForm.getCurrentPage() - 1) * machineTemperatureForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + machineTemperatureForm.getPageSize());
			rs = pst.executeQuery();
			List<MachineTemperatureBean> list = Lists.newArrayList();
			int id=0;
			while (rs.next()) {
				id++;
				MachineTemperatureBean bean=new MachineTemperatureBean();
				bean.setId(id);
				bean.setVersionId(rs.getInt("versionId"));
				bean.setBaseMachineId(rs.getInt("id"));
				bean.setFactoryNumber(rs.getString("factoryNumber"));
				bean.setVmCode(rs.getString("code"));
				bean.setPrincipal(rs.getString("linkMan"));
				bean.setMainProgramVersion(rs.getString("mainProgramVersion"));
				bean.setCanOnlineUpdate(rs.getInt("canOnlineUpdate"));
				//获取机器温度
				String findTemperature = findTemperature(rs.getString("factoryNumber"));
				bean.setTemperaturn(findTemperature);
				bean.setMachinesAddress(rs.getString("locatoinName"));
				if(bean.getCanOnlineUpdate()==1){
					String mainProgramVersion = bean.getMainProgramVersion();
					if(mainProgramVersion.length() >5){
						Integer type = Integer.valueOf(mainProgramVersion.substring(4,5));
						bean.setMainboardType(MainboardTypeEnum.foreach(type));
					}
				}
				bean.setMachineType(rs.getInt("machineType"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(machineTemperatureForm.getCurrentPage());
			data.setPageSize(machineTemperatureForm.getPageSize());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<MachineTemperatureDaoImpl>----<listPage>----end");
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
	 * 获取售货机 温度
	 */
	public String findTemperature(String factoryNumber) {
		//根据出厂编号 那都售货机心跳
		String content = redisClient.get("HM-" + factoryNumber);
		log.info("机器心跳=="+content);
		if(content!=null) {
			if(content.contains("c:")) {
				String[] split = content.split("c:");
				String substring = split[1].substring(0, 1);
				if(substring.equals("+")) {
					return split[1].substring(1, 3)+"℃";
				}else {
					return split[1].substring(0, 3)+"℃";
				}
			}else {
				return "机器无温度";
			}
		}
		return "机器无温度";
	}

	@Override
	public boolean updateMachinesProgramVersion(String factoryNum, String versionInfo) {
		log.info("<MachineTemperatureDaoImpl>----<updateMachinesVersion>----start");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE vending_machines_base SET mainProgramVersion = '"+versionInfo+"' WHERE factoryNumber = '"+factoryNum+"'");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int result  = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<MachineTemperatureDaoImpl>----<updateMachinesVersion>----end");
		if(result>0){
			return true;
		}
		return false;
	}

}
