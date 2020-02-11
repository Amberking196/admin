package com.server.module.system.machineManage.machineBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.server.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.system.machineManage.machinesWayItem.VendingMachinesWayItemBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-03-29 17:51:22
 */
@Repository
public class VendingMachinesBaseDaoImpl extends BaseDao<VendingMachinesBaseBean> implements VendingMachinesBaseDao {

	public static Logger log = LogManager.getLogger(VendingMachinesBaseDaoImpl.class);	
	 
	
	public ReturnDataUtil listPage(VendingMachinesBaseCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select vmb.cardSupplier,vmi.code as vmCode,vmb.id,machinesTypeId,aisleConfiguration,factoryNumber,mainProgramVersion,ipcNumber,liftingGearNumber,electricCabinetNumber,caseNumber,doorNumber,airCompressorNumber,keyStr,remark,mt.name,mt.state,vmb.simNumber,vmb.simExpireDate from vending_machines_base vmb ");
		sql.append(" left join machines_type mt on vmb.machinesTypeId=mt.id ");
		sql.append(" left join vending_machines_info vmi on vmi.machinesBaseId = vmb.id ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotEmpty(condition.getVmCode())){
			sql.append(" and vmi.code like '%"+condition.getVmCode()+"%' ");
		}
		if(condition.getId()!=null){
			sql.append(" and vmb.id='"+condition.getId()+"' ");
		}
		if(StringUtils.isNotEmpty(condition.getFactoryNumber())) {
			sql.append(" and vmb.factoryNumber like '%"+condition.getFactoryNumber()+"%' ");
		}
		if(condition.getMachinesTypeId()!=null){
			sql.append(" and machinesTypeId='"+condition.getMachinesTypeId()+"' ");
		}
		if(StringUtils.isNotEmpty(condition.getSimNumber())){
			sql.append(" and vmb.simNumber like '%"+condition.getSimNumber()+"%'");
		}
		if(StringUtil.isNotEmpty(condition.getSimExpireDate())){
			sql.append(" and vmb.simExpireDate <='"+condition.getSimExpireDate()+" 23:59:59'");
		}
		sql.append(" order by id desc");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("售货机基础信息列表sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

			rs = pst.executeQuery();
			List<VendingMachinesBaseBean> list = Lists.newArrayList();
			while (rs.next()) {
				VendingMachinesBaseBean bean = new VendingMachinesBaseBean();
				bean.setId(rs.getLong("id"));
				bean.setCardSupplier(rs.getString("cardSupplier"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setMachinesTypeId(rs.getInt("machinesTypeId"));
				bean.setAisleConfiguration(rs.getString("aisleConfiguration"));
				bean.setFactoryNumber(rs.getString("factoryNumber"));
				bean.setMainProgramVersion(rs.getString("mainProgramVersion"));
				bean.setIpcNumber(rs.getString("ipcNumber"));
				bean.setLiftingGearNumber(rs.getString("liftingGearNumber"));
				bean.setElectricCabinetNumber(rs.getString("electricCabinetNumber"));
				bean.setCaseNumber(rs.getString("caseNumber"));
				bean.setDoorNumber(rs.getString("doorNumber"));
				bean.setAirCompressorNumber(rs.getString("airCompressorNumber"));
				bean.setKeyStr(rs.getString("keyStr"));
				bean.setRemark(rs.getString("remark"));
				bean.setMachinesTypeName(rs.getString("name"));
				bean.setSimExpireDate(rs.getTimestamp("simExpireDate"));
				bean.setSimNumber(rs.getString("simNumber"));
				if(rs.getInt("state")==2100) {
					bean.setIsDisabled(0);
				}else {
					bean.setIsDisabled(1);
				}
				
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setPageSize(condition.getPageSize());
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

	public VendingMachinesBaseBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		VendingMachinesBaseBean entity = new VendingMachinesBaseBean();
		return super.del(entity);
	}

	public boolean update(VendingMachinesBaseBean entity) {
		return super.update(entity);
	}

	public VendingMachinesBaseBean insert(VendingMachinesBaseBean entity) {
		return super.insert(entity);
	}

	public List<VendingMachinesBaseBean> list(VendingMachinesBaseCondition condition) {
		return null;
	}

	@Override
	public VendingMachinesBaseBean getBase(Object factoryNumber,Integer companyId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select vbm.id,machinesTypeId,aisleConfiguration,factoryNumber,mainProgramVersion,ipcNumber,liftingGearNumber,electricCabinetNumber,caseNumber,doorNumber,airCompressorNumber,keyStr,remark,mt.name,mt.wayCount ");
		sql.append("from vending_machines_base vbm JOIN machines_type mt on vbm.machinesTypeId=mt.id WHERE vbm.factoryNumber='"+factoryNumber+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		VendingMachinesBaseBean bean = new VendingMachinesBaseBean();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean.setId(rs.getLong("id"));
				bean.setMachinesTypeId(rs.getInt("machinesTypeId"));
				bean.setAisleConfiguration(rs.getString("aisleConfiguration"));
				bean.setFactoryNumber(rs.getString("factoryNumber"));
				bean.setMainProgramVersion(rs.getString("mainProgramVersion"));
				bean.setIpcNumber(rs.getString("ipcNumber"));
				bean.setLiftingGearNumber(rs.getString("liftingGearNumber"));
				bean.setElectricCabinetNumber(rs.getString("electricCabinetNumber"));
				bean.setCaseNumber(rs.getString("caseNumber"));
				bean.setDoorNumber(rs.getString("doorNumber"));
				bean.setAirCompressorNumber(rs.getString("airCompressorNumber"));
				bean.setKeyStr(rs.getString("keyStr"));
				bean.setRemark(rs.getString("remark"));
				bean.setMachinesTypeName(rs.getString("name"));
				bean.setWayCount(rs.getInt("wayCount"));
				bean.setCompanyId(companyId);
			}
			if (showSql) {
				log.info(sql);
			}
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return bean;
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
	
	/**
	 * 校验出厂编号 是否存在
	 */
	public boolean checkFactoryNumber(String factoryNumber) {
		log.info("<VendingMachinesBaseDaoImpl>----<checkFactoryNumber>---start");
		StringBuilder sql = new StringBuilder();
		sql.append("select factoryNumber from vending_machines_base  where factoryNumber='"+factoryNumber+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("校验出厂编号sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				log.info("<VendingMachinesBaseDaoImpl>----<checkFactoryNumber>---end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<VendingMachinesBaseDaoImpl>----<checkFactoryNumber>---end");
		return false;
	}

	/**
	 * 根据售货机出厂编号 得到对应机器货道的商品规格 组成一个字符串 返回
	 */
	@Override
	public List<String> findItemStandard(String factnum) {
		log.info("<VendingMachinesBaseDaoImpl>----<findItemStandard>---start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select vmw.wayNumber,ib.standard,vmw.vendingMachinesCode from vending_machines_way vmw  ");
		sql.append(" left JOIN vending_machines_item vmit ON vmw.itemId=vmit.id ");
		sql.append(" left join item_basic ib  on vmit.basicItemId=ib.id ");
		sql.append(" left join vending_machines_info vmi on vmw.vendingMachinesCode=vmi.code ");
		sql.append(" left join vending_machines_base vmb on vmi.machinesBaseId=vmb.id ");
		sql.append(" where vmb.factoryNumber='"+factnum+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> list=Lists.newArrayList();
		
		log.info("根据售货机出厂编号 得到对应机器货道的商品规格sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next() && rs!=null) {
				String standard = rs.getString("standard");
				list.add(standard);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<VendingMachinesBaseDaoImpl>----<findItemStandard>---end");
		return list;
	}

	@Override
	public List<WayStandardDto> getStandardInfo(String factnum) {
		log.info("<VendingMachinesBaseDaoImpl>----<getStandardInfo>---start");
		StringBuilder sql = new StringBuilder();
//		sql.append(" select vmw.wayNumber,ib.standard,vmw.vendingMachinesCode from vending_machines_way vmw  ");
//		sql.append(" left JOIN vending_machines_item vmit ON vmw.itemId=vmit.id ");
//		sql.append(" left join item_basic ib  on vmit.basicItemId=ib.id ");
//		sql.append(" left join vending_machines_info vmi on vmw.vendingMachinesCode=vmi.code ");
//		sql.append(" left join vending_machines_base vmb on vmi.machinesBaseId=vmb.id ");
//		sql.append(" where vmb.factoryNumber='"+factnum+"'");
		
		sql.append(" SELECT vmwi.wayNumber,ib.standard,vmwi.vmCode FROM vending_machines_way_item vmwi");
		sql.append(" LEFT JOIN item_basic ib  ON vmwi.basicItemId=ib.id ");
		sql.append(" LEFT JOIN vending_machines_info vmi ON vmwi.vmCode=vmi.code ");
		sql.append(" LEFT JOIN vending_machines_base vmb ON vmi.machinesBaseId=vmb.id");
		sql.append(" WHERE vmb.factoryNumber='"+factnum+"'");
		sql.append(" ORDER BY wayNumber,orderNumber");
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		WayStandardDto way = null;
		List<WayStandardDto> list=Lists.newArrayList();
		
		log.info("根据售货机出厂编号 得到对应机器货道的商品规格sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next() && rs!=null) {
				way = new WayStandardDto();
				way.setStandard(rs.getString("standard"));
				way.setVmCode(rs.getString("vmCode"));
				way.setWayNum(rs.getInt("wayNumber"));
				list.add(way);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<VendingMachinesBaseDaoImpl>----<getStandardInfo>---end");
		return list;
	}
	
	public Integer getVersionByFactoryNumber(String factnum) {
		log.info("<VendingMachinesBaseDaoImpl>----<getVersionByFactoryNumber>---start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select vmi.machineVersion from vending_machines_info vmi  ");
		sql.append(" left join vending_machines_base vmb on vmi.machinesBaseId=vmb.id ");
		sql.append(" where vmb.factoryNumber='"+factnum+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Integer machineVersion = 0;
		log.info("根据售货机出厂编号 得到版本号sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next() && rs!=null) {
				 machineVersion = rs.getInt("machineVersion");
			}
			return machineVersion;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<VendingMachinesBaseDaoImpl>----<getVersionByFactoryNumber>---end");
		return machineVersion;
	}

	@Override
	public List<VendingMachinesWayItemBean> findItemWeight(String factnum) {
		log.info("<VendingMachinesBaseDaoImpl>----<findItemWeight>---start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select aisleConfiguration,vmwi.wayNumber,vmwi.weight from vending_machines_way_item vmwi  ");
		sql.append(" left JOIN vending_machines_info vmi ON vmwi.vmCode=vmi.code ");
		sql.append(" left join vending_machines_base vmb on vmi.machinesBaseId=vmb.id ");
		sql.append(" where vmb.factoryNumber='"+factnum+"' ");
		sql.append(" order by vmwi.wayNumber,vmwi.weight");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<VendingMachinesWayItemBean> list=Lists.newArrayList();

		log.info("根据售货机出厂编号 得到对应机器货道的商品规格sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next() && rs!=null) {
				VendingMachinesWayItemBean vmwib=new VendingMachinesWayItemBean();
				vmwib.setWayNumber(rs.getLong("wayNumber"));
				vmwib.setWeight(rs.getInt("weight"));
				vmwib.setAisleConfiguration(rs.getString("aisleConfiguration"));

				list.add(vmwib);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<VendingMachinesBaseDaoImpl>----<findItemWeight>---end");
		return list;
	}

	@Override
	public boolean updateProgramVersion(String programVersion, String factoryNum) {
		log.info("<VendingMachinesBaseDaoImpl>----<updateProgramVersion>---start");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE vending_machines_base SET mainProgramVersion = '"+programVersion+"' WHERE factoryNumber = '"+factoryNum+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int result = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<VendingMachinesBaseDaoImpl>----<updateProgramVersion>---end");
		return result > 0;
	}

	@Override
	public boolean updateCanOnlineUpdate(String factoryNum) {
		log.info("<VendingMachinesBaseDaoImpl>----<updateCanOnlineUpdate>---start");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE vending_machines_base SET canOnlineUpdate = 1 WHERE factoryNumber = '"+factoryNum+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int result = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<VendingMachinesBaseDaoImpl>----<updateCanOnlineUpdate>---end");
		return result > 0;
	}
}
