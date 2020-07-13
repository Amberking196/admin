package com.server.module.system.machineManage.machineList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.common.persistence.Page;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.baseManager.stateInfo.StateInfoDao;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.machineManage.machineCode.VendingMachinesCodeBean;
import com.server.module.system.machineManage.machineCode.VendingMachinesCodeDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.convert.ConvertCodeUtil;
import com.server.util.stateEnum.MainboardTypeEnum;


@Repository
public class VendingMachinesInfoDaoImpl extends BaseDao<VendingMachinesInfoBean> implements VendingMachinesInfoDao {

    public static Logger log = LogManager.getLogger(VendingMachinesInfoDaoImpl.class);
    @Autowired
    private CompanyDao companyDaoImpl;

    @Autowired
    private VendingMachinesCodeDao VendingMachinesCodeDaoImpl;

    @Autowired
    private StateInfoDao StateInfoDaoImpl;

    /***
     * 根据公司id查询公司当下的售卖机信息
     */
    @Override
    public List<String> findVmcodeByCompanyId(String companyIds) {
        log.info("<VendingMachinesInfoDaoImpl--findVmcodeByCompanyId--start>");
        List<String> result = new ArrayList<String>();
        StringBuffer sql = new StringBuffer();
        sql.append("select code from vending_machines_info where companyId in(" + companyIds + ")");
        log.info("查询语句：" + sql.toString());
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                result.add(rs.getString("code"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<VendingMachinesInfoDaoImpl--findVmcodeByCompanyId--end>");
        return result;
    }

    /**
     * 根据售卖机code查询售卖机信息
     */
    @Override
    public VendingMachinesInfoBean findVendingMachinesByCode(String code) {
        //log.info("<VendingMachinesInfoDaoImpl--findVendingMachinesByCode--start>");
        StringBuffer sql = new StringBuffer();

        sql.append ( " select startUsingTime,c.name as companyName,banWayNumber,code,machineType,companyId,errorWarn,isShowH5,itemType,lat,linkman,"
                + " locatoinDate,locatoinName,lon,machinesBaseId,vmi.name,respManId,vmi.state,vmi.areaId,vmi.untieRemark,machineVersion "
                + " from vending_machines_info vmi left join company c on c.id = vmi.companyId where  code=" + code );
        sql.append ( " and (companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or FIND_IN_SET( vmi.code,(select GROUP_CONCAT(vmCode) from login_info_machine lim where lim.userId="+UserUtils.getUser().getId()+")))");

        log.info("根据售卖机code查询售卖机信息--sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        VendingMachinesInfoBean vmInfoBean =  new VendingMachinesInfoBean();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                vmInfoBean.setBanWayNumber(rs.getString("banWayNumber"));
                vmInfoBean.setCode(rs.getString("code"));
                vmInfoBean.setCompanyId(rs.getInt("companyId"));
                vmInfoBean.setErrorWarn(rs.getInt("errorWarn"));
                vmInfoBean.setIsShowH5(rs.getInt("isShowH5"));
                vmInfoBean.setItemType(rs.getString("itemType"));
                vmInfoBean.setLat(rs.getDouble("lat"));
                vmInfoBean.setLinkman(rs.getString("linkman"));
                vmInfoBean.setLocatoinDate(rs.getDate("locatoinDate"));
                vmInfoBean.setLocatoinName(rs.getString("locatoinName"));
                vmInfoBean.setLon(rs.getDouble("lon"));
                vmInfoBean.setMachinesBaseId(rs.getInt("machinesBaseId"));
                vmInfoBean.setName(rs.getString("name"));
                vmInfoBean.setRespManId(rs.getLong("respManId"));
                vmInfoBean.setState(rs.getInt("state"));
                String stateName = StateInfoDaoImpl.getNameByState(rs.getLong("state"));
                if (stateName != null) {
                	vmInfoBean.setStateName(stateName);
                }
                vmInfoBean.setAreaId(rs.getInt("areaId"));
                vmInfoBean.setMachineType(rs.getInt("machineType"));
                vmInfoBean.setUntieRemark(rs.getString("untieRemark"));
                vmInfoBean.setMachineVersion(rs.getInt("machineVersion"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeConnection(rs, ps, conn);
        }
       // log.info("<VendingMachinesInfoDaoImpl--findVendingMachinesByCode--end>");
        return vmInfoBean;
    }


    /**
     * 根据售卖机code查询售卖机信息
     */
    @Override
    public VendingMachinesInfoBean findVendingMachinesByCodeNew(String code) {
        StringBuffer sql = new StringBuffer();
        sql.append ( " select startUsingTime,c.name as companyName,banWayNumber,code,machineType,companyId,errorWarn,isShowH5,itemType,lat,linkman,"
                + " locatoinDate,locatoinName,lon,machinesBaseId,vmi.name,respManId,vmi.state,vmi.areaId,vmi.untieRemark,machineVersion "
                + " from vending_machines_info vmi left join company c on c.id = vmi.companyId where  code=" + code );
        sql.append ( " and companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
        log.info("根据售卖机code查询售卖机信息--sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        VendingMachinesInfoBean vmInfoBean =  null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                vmInfoBean=new  VendingMachinesInfoBean();
                vmInfoBean.setBanWayNumber(rs.getString("banWayNumber"));
                vmInfoBean.setCode(rs.getString("code"));
                vmInfoBean.setCompanyId(rs.getInt("companyId"));
                vmInfoBean.setErrorWarn(rs.getInt("errorWarn"));
                vmInfoBean.setIsShowH5(rs.getInt("isShowH5"));
                vmInfoBean.setItemType(rs.getString("itemType"));
                vmInfoBean.setLat(rs.getDouble("lat"));
                vmInfoBean.setLinkman(rs.getString("linkman"));
                vmInfoBean.setLocatoinDate(rs.getDate("locatoinDate"));
                vmInfoBean.setLocatoinName(rs.getString("locatoinName"));
                vmInfoBean.setLon(rs.getDouble("lon"));
                vmInfoBean.setMachinesBaseId(rs.getInt("machinesBaseId"));
                vmInfoBean.setName(rs.getString("name"));
                vmInfoBean.setRespManId(rs.getLong("respManId"));
                vmInfoBean.setState(rs.getInt("state"));
                String stateName = StateInfoDaoImpl.getNameByState(rs.getLong("state"));
                if (stateName != null) {
                    vmInfoBean.setStateName(stateName);
                }
                vmInfoBean.setAreaId(rs.getInt("areaId"));
                vmInfoBean.setMachineType(rs.getInt("machineType"));
                vmInfoBean.setUntieRemark(rs.getString("untieRemark"));
                vmInfoBean.setMachineVersion(rs.getInt("machineVersion"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeConnection(rs, ps, conn);
        }
        return vmInfoBean;
    }
    
    /**
     * 根据售卖机codes批量查询售卖机信息
     */
    @Override
    public VendingMachinesInfoBean findVendingMachinesByCodeForMap(String code) {
        //log.info("<VendingMachinesInfoDaoImpl--findVendingMachinesByCode--start>");
        StringBuffer sql = new StringBuffer();

        sql.append ( " select startUsingTime,c.name as companyName,banWayNumber,code,companyId,errorWarn,isShowH5,itemType,lat,linkman,"
                + " locatoinDate,locatoinName,lon,machinesBaseId,vmi.name,respManId,vmi.state,vmi.areaId,machineVersion "
                + " from vending_machines_info vmi left join company c on c.id = vmi.companyId where  code=" + code );
        log.info("根据售卖机codes批量查询售卖机信息：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        VendingMachinesInfoBean vmInfoBean = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                vmInfoBean = new VendingMachinesInfoBean();
                vmInfoBean.setBanWayNumber(rs.getString("banWayNumber"));
                vmInfoBean.setCode(rs.getString("code"));
                vmInfoBean.setCompanyId(rs.getInt("companyId"));
                vmInfoBean.setErrorWarn(rs.getInt("errorWarn"));
                vmInfoBean.setIsShowH5(rs.getInt("isShowH5"));
                vmInfoBean.setItemType(rs.getString("itemType"));
                vmInfoBean.setLat(rs.getDouble("lat"));
                vmInfoBean.setLinkman(rs.getString("linkman"));
                vmInfoBean.setLocatoinDate(rs.getDate("locatoinDate"));
                vmInfoBean.setLocatoinName(rs.getString("locatoinName"));
                vmInfoBean.setLon(rs.getDouble("lon"));
                vmInfoBean.setMachinesBaseId(rs.getInt("machinesBaseId"));
                vmInfoBean.setName(rs.getString("name"));
                vmInfoBean.setRespManId(rs.getLong("respManId"));
                vmInfoBean.setState(rs.getInt("state"));
                String stateName = StateInfoDaoImpl.getNameByState(rs.getLong("state"));
                if (stateName != null) {
                	vmInfoBean.setStateName(stateName);
                }
                vmInfoBean.setAreaId(rs.getInt("areaId"));
                vmInfoBean.setMachineVersion(rs.getInt("machineVersion"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeConnection(rs, ps, conn);
        }
       // log.info("<VendingMachinesInfoDaoImpl--findVendingMachinesByCode--end>");
        return vmInfoBean;
    }
    
    public void insert() {
        VendingMachinesInfoBean info = new VendingMachinesInfoBean();
        info.setCode("code0000");
        // info.setCompanyId(100);
        // info.setItemType(null);
        // info.setMachinesBaseId(12345l);
        info.setLinkman("dbh1111");
        try {
            VendingMachinesInfoBean info1 = super.get("code0000");
            System.out.println("info111=" + info1.getLinkman());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // @Override
    public Page listPage(Page page, String sql, List<Object> parameterList) {
        // TODO Auto-generated method stub
        return super.listPage(page, sql, parameterList, MachinesInfoAndBaseDto.class);

    }

    /**
     * 售货机列表 查询
     */
    @SuppressWarnings("resource")
    public ReturnDataUtil listPage(VendingMachinesInfoCondition condition) {
        log.info("<VendingMachinesInfoDaoImpl--listPage--start>");
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select  b.simNumber as card,vl.name lineName,i.code,i.companyId,state,machinesBaseId,lon,lat,i.district,i.districtName,banWayNumber,i.name,locatoinName,i.updateTime,i.createTime,i.editTime,i.startUsingTime,i.machineType,itemType,locatoinDate,respManId,linkman,errorWarn,isShowH5 ,lineId,");
        sql.append(
                " qrCode,b.id,machinesTypeId,aisleConfiguration,factoryNumber,mainProgramVersion,ipcNumber,liftingGearNumber,electricCabinetNumber,caseNumber,doorNumber,airCompressorNumber,keyStr,remark, ");
        sql.append(
                " i.areaId,va.name areaName,IF(i.state=20001,TIMESTAMPDIFF(DAY,i.editTime,NOW()),TIMESTAMPDIFF(DAY,i.startUsingTime,NOW())) as startUsingDay, i.untieRemark,i.machineVersion,rcm.companyId as replenishCompanyId,b.canOnlineUpdate from vending_machines_info i left join vending_machines_base b on i.machinesBaseId=b.id ");
        sql.append(
                " left join vending_line vl on i.lineId=vl.id left join vending_area va on i.areaId=va.id  ");
        sql.append(" left join replenish_company_machines rcm on i.code=rcm.code where 1=1 ");
        sql.append(" and i.companyId in "
                + companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
        List<Object> plist = Lists.newArrayList();
        if (condition.getCompanyId() != null && condition.getCompanyId() > 1) {
            sql.append(" and i.companyId in " + companyDaoImpl.findAllSonCompanyIdForInSql(condition.getCompanyId()));
        }
        if (StringUtil.isNotEmpty(condition.getCode())) {
            sql.append(" and i.code like ? ");
            plist.add("%" + condition.getCode() + "%");
        }
        if (StringUtil.isNotEmpty(condition.getFactoryNumber())) {
            sql.append(" and b.factoryNumber like ? ");
            plist.add("%" + condition.getFactoryNumber() + "%");
        }
        if (condition.getAreaId() != null && condition.getAreaId()>0) {
            sql.append(" and i.areaId= ? ");
            plist.add(condition.getAreaId());
        }
        if (condition.getLineId() != null) {
            sql.append(" and i.lineId= ? ");
            plist.add(condition.getLineId());
        }
        if (condition.getState() != null) {
            sql.append(" and i.state=? ");
            plist.add(condition.getState());
        }
        if(condition.getDistrict()!=null){
            sql.append(" and i.district=? ");
            plist.add(condition.getDistrict());
        }else {
        	if(condition.getCity()!=null) {
        		Integer c1=condition.getCity();
        		Integer c2=c1+100;
        		 sql.append(" and i.district>? ");
                 plist.add(c1);
                 sql.append(" and i.district<? ");
                 plist.add(c2);
        	}
        }
        if (condition.getStartDate() != null) {
            sql.append(" and i.locatoinDate >= ? ");
            plist.add(DateUtil.formatYYYYMMDDHHMMSS(condition.getStartDate()));
        }
        if (condition.getEndDate() != null) {
            sql.append(" and i.locatoinDate < ? ");
            plist.add(DateUtil.formatLocalYYYYMMDDHHMMSS(condition.getEndDate(), 1));
        }
        if (StringUtil.isNotEmpty(condition.getLocatoinName())) {
            sql.append(" and i.locatoinName like ? ");
            plist.add("%" + condition.getLocatoinName() + "%");
        }
        if (condition.getIsShowAll() == 1) {
            sql.append(" and i.state=20001 ");
        }
        if(condition.getMachineVersion()!=null) {
        	sql.append(" and i.machineVersion = '"+condition.getMachineVersion()+"'");
        }
        if(condition.getMainProgramVersion()!=null && !condition.getMainProgramVersion().equals("undefined")) {
        	sql.append(" and b.mainProgramVersion like  ?");
            plist.add("%" + condition.getMainProgramVersion() + "%");
        }
        sql.append("order by i.updateTime desc,i.code");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("售货机列表sql语句：" + sql.toString());
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
            if (condition.getIsShowAll() == 1) {
                pst = conn.prepareStatement(sql.toString());
            } else {
                long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
                pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
            }
            if (plist != null && plist.size() > 0)
                for (int i = 0; i < plist.size(); i++) {
                    pst.setObject(i + 1, plist.get(i));
                }
            rs = pst.executeQuery();
            List<MachinesInfoAndBaseDto> list = Lists.newArrayList();
            while (rs.next()) {
                MachinesInfoAndBaseDto bean = new MachinesInfoAndBaseDto();
                bean.setCard(rs.getString("card"));
                bean.setCode(rs.getString("code"));
                bean.setCompanyId(rs.getInt("companyId"));
                bean.setState(rs.getInt("state"));
                bean.setCreateTime(rs.getTimestamp("createTime"));
                // 如果baseId(出厂编号等于0，那说明它在数据库中是为null的)
                Long baseId = rs.getLong("machinesBaseId");
                if (baseId == 0) {
                    baseId = null;
                }
                bean.setMachinesBaseId(baseId);
                bean.setLon(rs.getDouble("lon"));
                bean.setLat(rs.getDouble("lat"));
                bean.setBanWayNumber(rs.getString("banWayNumber"));
                bean.setName(rs.getString("name"));
                bean.setLocatoinName(rs.getString("locatoinName"));
                bean.setItemType(rs.getString("itemType"));
                bean.setLocatoinDate(rs.getDate("locatoinDate"));
                bean.setRespManId(rs.getLong("respManId"));
                bean.setLinkman(rs.getString("linkman"));
                bean.setErrorWarn(rs.getInt("errorWarn"));
                bean.setIsShowH5(rs.getInt("isShowH5"));
                bean.setLineId(rs.getInt("lineId"));
                bean.setQrCode(rs.getString("qrCode"));
                bean.setLineName(rs.getString("lineName"));
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
                //正式启用时间
                bean.setStartUsingTime(rs.getDate("startUsingTime"));
                bean.setStartUsingDay(rs.getString("startUsingDay"));
                bean.setMachineType(rs.getInt("machineType"));
                bean.setUntieRemark(rs.getString("untieRemark"));
                bean.setMachineVersion(rs.getInt("machineVersion"));
                bean.setDistrict(rs.getInt("district"));
                bean.setDistrictName(rs.getString("districtName"));
                // bean.setMachinesTypeName(machinesTypeName);
                // 售货机状态
                String stateName = StateInfoDaoImpl.getNameByState(rs.getLong("state"));
                if (stateName != null) {
                    bean.setStateName(stateName);
                }
                bean.setCanOnlineUpdate(rs.getInt("canOnlineUpdate"));
                if(bean.getCanOnlineUpdate()==1){
					String mainProgramVersion = bean.getMainProgramVersion();
					if(mainProgramVersion.length() >5){
						Integer type = Integer.valueOf(mainProgramVersion.substring(4,5));
						bean.setMainboardType(MainboardTypeEnum.foreach(type));
					}
				}
                CompanyBean companyBean = companyDaoImpl.findCompanyById(rs.getInt("companyId"));
                bean.setCompanyName(companyBean.getName());
                bean.setAreaId(rs.getInt("areaId"));
                bean.setAreaName(rs.getString("areaName"));
                //添加补货公司id和名称
                bean.setReplenishCompanyId(rs.getInt("replenishCompanyId"));
                CompanyBean companyBean02 = companyDaoImpl.findCompanyById(bean.getReplenishCompanyId());
                if(companyBean02==null) {
                	bean.setReplenishCompanyName(companyBean.getName());
                }else {
                	bean.setReplenishCompanyName(companyBean02.getName());
                	
                }
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
                log.info(plist.toString());
            }
            data.setCurrentPage(condition.getCurrentPage());
            data.setPageSize(condition.getPageSize());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            log.info("<VendingMachinesInfoDaoImpl--listPage--end>");
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
     * 根据公司id查询公司当下的售卖机信息
     */
    @Override
    public List<VendingMachinesInfoBean> findVmByCompanyId(Integer companyId) {
        log.info("<MachinesDaoImpl--findVmByCompanyId--start>");
        StringBuffer sql = new StringBuffer();
        sql.append(
                "select banWayNumber,code,companyId,errorWarn,isShowH5,itemType,lat,linkman,locationDate,locationName,");
        sql.append("lon,machinesBaseId,name,respManId,state from vending_machines_info where companyId = " + companyId);
        log.info("sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        VendingMachinesInfoBean vmInfoBean = null;
        List<VendingMachinesInfoBean> vmInfoList = new ArrayList<VendingMachinesInfoBean>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                vmInfoBean = new VendingMachinesInfoBean();
                vmInfoBean.setBanWayNumber(rs.getString("banWayNumber"));
                vmInfoBean.setCode(rs.getString("code"));
                vmInfoBean.setCompanyId(rs.getInt("companyId"));
                vmInfoBean.setErrorWarn(rs.getInt("errorWarn"));
                vmInfoBean.setIsShowH5(rs.getInt("isShowH5"));
                vmInfoBean.setItemType(rs.getString("itemType"));
                vmInfoBean.setLat(rs.getDouble("lat"));
                vmInfoBean.setLinkman(rs.getString("linkman"));
                vmInfoBean.setLocatoinDate(rs.getDate("locationDate"));
                vmInfoBean.setLocatoinName(rs.getString("locationName"));
                vmInfoBean.setLon(rs.getDouble("lon"));
                vmInfoBean.setMachinesBaseId(rs.getInt("machinesBaseId"));
                vmInfoBean.setName(rs.getString("name"));
                vmInfoBean.setRespManId(rs.getLong("respManId"));
                vmInfoBean.setState(rs.getInt("state"));
                vmInfoList.add(vmInfoBean);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeConnection(rs, ps, conn);
        }
        log.info("<MachinesDaoImpl--findVmByCompanyId--end>");
        return vmInfoList;
    }

    /**
     * 添加售货机
     */
    @Override
    public VendingMachinesInfoBean addMachines(VendingMachinesInfoBean bean) {
        // TODO Auto-generated method stub
        VendingMachinesInfoBean Infobean = super.insert(bean);
        if (Infobean != null) {
            return Infobean;
        }
        return null;
    }

    /**
     * 生成 售货机编号
     *
     * @param areadNumber 地区编号
     * @param typeId      售货机类型编号
     * @return
     */
    public synchronized String getVendingMachines(String areadNumber, Long typeId) {
        Long code = 0L;
        String typeIdStr = typeId < 10 ? "0" + typeId : typeId.toString();
        log.debug("typeIdStr:" + typeIdStr);
        String newCode = areadNumber + typeIdStr;
        log.debug("areadNumber:" + areadNumber);
        // 查询是否有记录
        log.debug("typeId:" + typeId);
        VendingMachinesCodeBean vmc = VendingMachinesCodeDaoImpl.getByUnique(areadNumber, typeId);
        if (vmc == null) {
            vmc = new VendingMachinesCodeBean();
            vmc.setAreaNumber(areadNumber);
            vmc.setCode(1L);
            vmc.setMachinesTypeId(typeId);
            VendingMachinesCodeDaoImpl.insert(vmc);
        } else {
            code = vmc.getCode();
            vmc.setCode(vmc.getCode() + 1);
            VendingMachinesCodeDaoImpl.update(vmc);
        }
        newCode += ConvertCodeUtil.codeConvertString(code);
        log.debug("code:" + code);
        return newCode;
    }

    /**
     * 根据收货机编号 查询出相关信息
     */
    @Override
    public MachinesInfoAndBaseDto getMachinesInfoAndBase(String code) {
        log.info("<VendingMachinesInfoDaoImpl--getMachinesInfoAndBase--start>");
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT code,companyId,state,machinesBaseId,lon,lat,banWayNumber,name,locatoinName,itemType,machineType,locatoinDate,respManId,linkman,errorWarn,isShowH5 ,id,machinesTypeId,aisleConfiguration,factoryNumber,mainProgramVersion,ipcNumber,liftingGearNumber,electricCabinetNumber,caseNumber,doorNumber,airCompressorNumber,keyStr,remark  from vending_machines_info i,vending_machines_base b ");
        sql.append("where i.machinesBaseId=b.id and i.code='" + code + "'");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("查询语句>>>" + sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            MachinesInfoAndBaseDto bean = new MachinesInfoAndBaseDto();
            while (rs.next()) {
                bean.setCode(rs.getString("code"));
                bean.setCompanyId(rs.getInt("companyId"));
                bean.setState(rs.getInt("state"));
                bean.setMachinesBaseId(rs.getLong("machinesBaseId"));
                bean.setLon(rs.getDouble("lon"));
                bean.setLat(rs.getDouble("lat"));
                bean.setBanWayNumber(rs.getString("banWayNumber"));
                bean.setName(rs.getString("name"));
                bean.setLocatoinName(rs.getString("locatoinName"));
                bean.setItemType(rs.getString("itemType"));
                bean.setLocatoinDate(rs.getDate("locatoinDate"));
                bean.setRespManId(rs.getLong("respManId"));
                bean.setLinkman(rs.getString("linkman"));
                bean.setErrorWarn(rs.getInt("errorWarn"));
                bean.setIsShowH5(rs.getInt("isShowH5"));
                //判断机器的类型
                bean.setMachineType(rs.getInt("machineType"));
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
                // bean.setMachinesTypeName(machinesTypeName);
                // 售货机状态
                int state = rs.getInt("state");
                Long stateId = new Long((long) state);
                String stateName = StateInfoDaoImpl.getNameByState(stateId);
                bean.setStateName(stateName);
                return bean;
            }
            if (showSql) {
                log.info(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                rs.close();
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        log.info("<VendingMachinesInfoDaoImpl--getMachinesInfoAndBase--end>");
        return null;

    }

    /**
     * 公司售货机 的编辑
     */
    @Override
    public boolean updateEntity(VendingMachinesInfoBean entity) {
        // TODO Auto-generated method stub
        return super.update(entity);
    }

    /**
     * 根据路线lineId查询所有机器信息
     */
    @Override
    public List<VendingMachinesInfoBean> findMachinesByLineId(Integer lineId) {
        log.info("<VendingMachinesInfoDaoImpl--findMachinesByLineId--start>");
        String sql = " select banWayNumber,code,companyId,errorWarn,isShowH5,itemType,lat,linkman,"
                + " locatoinDate,locatoinName,lon,machinesBaseId,name,respManId,state "
                + " from vending_machines_info where lineId=" + lineId;
        log.info("查询语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        VendingMachinesInfoBean vmInfoBean = null;
        List<VendingMachinesInfoBean> machinesInfoList = new ArrayList<VendingMachinesInfoBean>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                vmInfoBean = new VendingMachinesInfoBean();
                vmInfoBean.setBanWayNumber(rs.getString("banWayNumber"));
                vmInfoBean.setCode(rs.getString("code"));
                vmInfoBean.setCompanyId(rs.getInt("companyId"));
                vmInfoBean.setErrorWarn(rs.getInt("errorWarn"));
                vmInfoBean.setIsShowH5(rs.getInt("isShowH5"));
                vmInfoBean.setItemType(rs.getString("itemType"));
                vmInfoBean.setLat(rs.getDouble("lat"));
                vmInfoBean.setLinkman(rs.getString("linkman"));
                vmInfoBean.setLocatoinDate(rs.getDate("locatoinDate"));
                vmInfoBean.setLocatoinName(rs.getString("locatoinName"));
                vmInfoBean.setLon(rs.getDouble("lon"));
                vmInfoBean.setMachinesBaseId(rs.getInt("machinesBaseId"));
                vmInfoBean.setName(rs.getString("name"));
                vmInfoBean.setRespManId(rs.getLong("respManId"));
                vmInfoBean.setState(rs.getInt("state"));
                vmInfoBean.setLineId(lineId);
                machinesInfoList.add(vmInfoBean);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeConnection(rs, ps, conn);
        }
        log.info("<VendingMachinesInfoDaoImpl--findMachinesByLineId--end>");
        return machinesInfoList;
    }

    /**
     * 支付宝二维码 查询
     */
    @Override
    public String getVendingMachinesInfoBean(String code) {
        log.info("<VendingMachinesInfoDaoImpl--getVendingMachinesInfoBean--start>");
        StringBuffer sql = new StringBuffer();
        sql.append("select url  from vending_machines_info where code='" + code + "'");
        log.info("二维码查询语句：" + sql.toString());
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                String url = rs.getString("url");
                return url.substring(url.lastIndexOf("/")+1, url.length()).trim();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<VendingMachinesInfoDaoImpl--getVendingMachinesInfoBean--end>");
        return null;

    }

    /**
     * 查询所有的售货机编号
     */
    @Override
    public List<String> getCode() {
        log.info("<VendingMachinesInfoDaoImpl--getCode--start>");
        List<String> result = new ArrayList<String>();
        StringBuffer sql = new StringBuffer();
        sql.append("select code from vending_machines_info");
        log.info("查询语句：" + sql.toString());
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                result.add(rs.getString("code"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<VendingMachinesInfoDaoImpl--getCode--end>");
        return result;
    }

    /**
     * 售货机列表的编辑
     */
    @Override
    public boolean updateInfoAndBase(MachinesInfoAndBaseDto dto) {
        log.info("<VendingMachinesInfoDaoImpl--updateInfoAndBase--start>");
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder();

        if (dto.getState() != null || dto.getRemark() != null || dto.getLocatoinName() != null || dto.getLat() != null
                || dto.getLon() != null) {
            sql.append("update vending_machines_info i,vending_machines_base b SET ");
            if (dto.getRemark() != null) {
                sql.append(" remark='" + dto.getRemark() + "',");
            }
            if (dto.getState() != null) {
                sql.append(" state='" + dto.getState() + "',");
            }
            if (dto.getLocatoinName() != null) {
                sql.append(" locatoinName='" + dto.getLocatoinName() + "',");
            }
            // 增加经纬度编辑
            if (dto.getLat() != null) {
                sql.append(" lat='" + dto.getLat() + "',");
            }
            if (dto.getLon() != null) {
                sql.append(" lon='" + dto.getLon() + "',");
            }
            if (dto.getEditTime()!=null){
                sql.append("editTime ='" +DateUtil.formatYYYYMMDDHHMMSS(dto.getEditTime())+"',");
            }
            sql = new StringBuilder(sql.substring(0, sql.length() - 1));
            sql.append(" WHERE i.machinesBaseId=b.id  AND b.id='" + dto.getId() + "'");
            log.info("售货机列表编辑>>sql语句>>：" + sql.toString());
            Connection conn = null;
            PreparedStatement ps = null;
            try {
                conn = openConnection();
                ps = conn.prepareStatement(sql.toString());
                int result = ps.executeUpdate();
                if (result > 0) {
                    return true;
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                this.closeConnection(null, ps, conn);
            }
        }
        log.info("<VendingMachinesInfoDaoImpl--updateInfoAndBase--end>");
        return false;
    }

    /**
     * 判断是否该机器模板下 已经存在一台售货机
     */
    @Override
    public boolean getMachinesBaseId(Integer machinesId) {
        log.info("<VendingMachinesInfoDaoImpl--getMachinesBaseId--start>");
        StringBuffer sql = new StringBuffer();
        sql.append("select * from vending_machines_info where machinesBaseId='" + machinesId + "' ");
        log.info("查询语句：" + sql.toString());
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<VendingMachinesInfoDaoImpl--getMachinesBaseId--end>");
        return false;
    }

    /**
     * 查询当前登录用户所属公司 所有的售货机编号
     */
    @Override
    public List<String> findCode(Integer state, Integer companyId,Integer areaId) {
        log.info("<VendingMachinesInfoDaoImpl--<findCode>--start>");
        List<String> result = new ArrayList<String>();
        StringBuffer sql = new StringBuffer();
        if (companyId != null) {
            sql.append("select code from vending_machines_info vmi where state = " + state + " and companyId in "
                    + companyDaoImpl.findAllSonCompanyIdForInSql(companyId));
        } else {
            sql.append("select code from vending_machines_info vmi where state = " + state + " and companyId in "
                    + companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
        }
		if(areaId!=null && areaId>0){
			sql.append("and vmi.areaId = '" + areaId + "' ");
		}
        log.info("查询登录用户下的售货机编号语句：" + sql.toString());
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                result.add(rs.getString("code"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<VendingMachinesInfoDaoImpl--<findCode>--end>");
        return result;
    }

    /**
     * 新二维码 路径
     */
    @Override
    public String getQRCode(String code) {
        log.info("<VendingMachinesInfoDaoImpl--getQRCode--start>");
        StringBuffer sql = new StringBuffer();
        sql.append(" select qrCode  from vending_machines_info where code='" + code + "'");
        log.info("二维码查询语句：" + sql.toString());
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                String qrCode = rs.getString("qrCode");
                return qrCode.substring(qrCode.lastIndexOf("/")+1, qrCode.length()).trim();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<VendingMachinesInfoDaoImpl--getQRCode--end>");
        return null;
    }

    /**
     * 根据当前用户 查询出本公司以及子公司的售货机信息
     */
    @Override
    public List<VendingMachinesInfoBean> getMachinesInfo(String code, Long companyId) {
        log.info("<VendingMachinesInfoDaoImpl>-----<getMachinesInfo>----start");
        StringBuffer sql = new StringBuffer();
        sql.append(
                "select banWayNumber,code,companyId,errorWarn,isShowH5,itemType,lat,linkman,locatoinDate,locatoinName,");
        sql.append("lon,machinesBaseId,name,respManId,state from vending_machines_info where companyId='" + companyId
                + "' ");
        sql.append(" and code='" + code + "' ");
        log.info("判断售货机编号是否存在sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        VendingMachinesInfoBean vmInfoBean = null;
        List<VendingMachinesInfoBean> list = new ArrayList<VendingMachinesInfoBean>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                vmInfoBean = new VendingMachinesInfoBean();
                vmInfoBean.setBanWayNumber(rs.getString("banWayNumber"));
                vmInfoBean.setCode(rs.getString("code"));
                vmInfoBean.setCompanyId(rs.getInt("companyId"));
                vmInfoBean.setErrorWarn(rs.getInt("errorWarn"));
                vmInfoBean.setIsShowH5(rs.getInt("isShowH5"));
                vmInfoBean.setItemType(rs.getString("itemType"));
                vmInfoBean.setLat(rs.getDouble("lat"));
                vmInfoBean.setLinkman(rs.getString("linkman"));
                vmInfoBean.setLocatoinDate(rs.getDate("locatoinDate"));
                vmInfoBean.setLocatoinName(rs.getString("locatoinName"));
                vmInfoBean.setLon(rs.getDouble("lon"));
                vmInfoBean.setMachinesBaseId(rs.getInt("machinesBaseId"));
                vmInfoBean.setName(rs.getString("name"));
                vmInfoBean.setRespManId(rs.getLong("respManId"));
                vmInfoBean.setState(rs.getInt("state"));
                list.add(vmInfoBean);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeConnection(rs, ps, conn);
        }
        log.info("<MachinesDaoImpl--getMachinesInfo--end>");
        return list;
    }

    /**
     * 根据线路Id查询所有的售货机
     */
    @Override
    public ReturnDataUtil findMachinesListPage(VendingMachinesInfoCondition condition) {
        log.info("<VendingMachinesInfoDaoImpl--listPage--start>");
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select banWayNumber,code,companyId,errorWarn,isShowH5,itemType,lat,linkman, locatoinDate,locatoinName,lon,machinesBaseId,name,respManId,state from vending_machines_info ");
        if (condition.getLineId() != null) {
            sql.append(" where lineId='" + condition.getLineId() + "'");
        }
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("根据线路Id查询所有的售货机sql语句：" + sql.toString());
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
            List<VendingMachinesInfoBean> list = Lists.newArrayList();
            while (rs != null && rs.next()) {
                VendingMachinesInfoBean vmInfoBean = new VendingMachinesInfoBean();
                vmInfoBean.setBanWayNumber(rs.getString("banWayNumber"));
                vmInfoBean.setCode(rs.getString("code"));
                vmInfoBean.setCompanyId(rs.getInt("companyId"));
                vmInfoBean.setErrorWarn(rs.getInt("errorWarn"));
                vmInfoBean.setIsShowH5(rs.getInt("isShowH5"));
                vmInfoBean.setItemType(rs.getString("itemType"));
                vmInfoBean.setLat(rs.getDouble("lat"));
                vmInfoBean.setLinkman(rs.getString("linkman"));
                vmInfoBean.setLocatoinDate(rs.getDate("locatoinDate"));
                vmInfoBean.setLocatoinName(rs.getString("locatoinName"));
                vmInfoBean.setLon(rs.getDouble("lon"));
                vmInfoBean.setMachinesBaseId(rs.getInt("machinesBaseId"));
                vmInfoBean.setName(rs.getString("name"));
                vmInfoBean.setRespManId(rs.getLong("respManId"));
                vmInfoBean.setState(rs.getInt("state"));
                list.add(vmInfoBean);
            }
            if (showSql) {
                log.info(sql);
            }
            data.setCurrentPage(condition.getCurrentPage());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            log.info("<VendingMachinesInfoDaoImpl--listPage--end>");
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

    @Override
    public ReturnDataUtil untieVendingMachine(UntieBean untieBean) {

        log.info("<VendingMachinesInfoDaoImpl--untieVendingMachine--start>");
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE vending_machines_info ");
        //修改把machinesBaseId=null改为machinesBaseId=0
        sql.append(" SET machinesBaseId = 0, updateTime = current_timestamp, untieUserId = " + untieBean.getUserId() + ", untieVmCode = " + untieBean.getFactoryNumber() + ",state=20007");

        if (untieBean.getRemark() != null) {
            sql.append(" , untieRemark = '" + untieBean.getRemark() + "'");
        }

        sql.append(" WHERE code = '" + untieBean.getVmCode() + "'");

        Connection conn = null;
        PreparedStatement pst = null;
        log.info("解除绑定的SQL：" + sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            int num = pst.executeUpdate();
            if (showSql) {
                log.info(sql);
            }
            if (num > 0) {
                data.setStatus(1);
                data.setMessage("解绑成功！");
            } else {
                data.setStatus(0);
                data.setMessage("解绑失败！");
            }

            log.info("<VendingMachinesInfoDaoImpl--untieVendingMachine--end>");
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            data.setStatus(0);
            data.setMessage("解绑失败！");
            return data;
        } finally {
            try {
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

	@Override
	public boolean updateEntity(VendingMachinesInfoBean entity, Connection conn) {
		return super.update(conn, entity);
	}

	@Override
	public VendingMachinesInfoBean addMachines(VendingMachinesInfoBean bean, Connection conn) {
		
		return (VendingMachinesInfoBean)(super.insert(conn, bean));
	}
	/**
     * 售货机列表 查询
     */
    @SuppressWarnings("resource")
    public ReturnDataUtil nearbyListPage(VendingMachinesInfoCondition condition) {
        log.info("<VendingMachinesInfoDaoImpl--listPage--start>");
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from vending_machines_info i where 1=1 ");
        sql.append(" and i.companyId in " + companyDaoImpl.findAllSonCompanyIdForInSql(1));
        sql.append(" and (i.state=20001 or i.state=20003)");
        if(condition.getMaxlon() != 0 ) {
        	sql.append(" and lon <"+condition.getMaxlon());
        }
        if(condition.getMinlon() != 0 ) {
        	sql.append(" and lon >"+condition.getMinlon());
        }
        if(condition.getMaxlat() != 0 ) {
        	sql.append(" and lat <"+condition.getMaxlat());
        }
        if(condition.getMinlat() != 0 ) {
        	sql.append(" and lat >"+condition.getMinlat());
        }
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("售货机列表sql语句：" + sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            List<MachinesInfoAndBaseDto> list = Lists.newArrayList();
            while (rs.next()) {
                MachinesInfoAndBaseDto bean = new MachinesInfoAndBaseDto();
                bean.setMachineVersion(rs.getInt("machineVersion"));
                bean.setCode(rs.getString("code"));
                bean.setLon(rs.getDouble("lon"));
                bean.setLat(rs.getDouble("lat"));
                bean.setLocatoinName(rs.getString("locatoinName"));
                String stateName = StateInfoDaoImpl.getNameByState(rs.getLong("state"));
                if (stateName != null) {
                    bean.setStateName(stateName);
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
            log.info("<VendingMachinesInfoDaoImpl--listPage--end>");
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return data;
        } finally {
            this.closeConnection(rs, pst, conn);
        }

    }
    
    @Override
	public MachinesLAC getMachinesLAC(String vmCode) {
		log.info("<VendingMachinesInfoDaoImpl--getMachinesLAC--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmi.`code` AS vmCode,vmi.`lineId`,vmi.`companyId`,vl.`areaId` ");
		sql.append(" FROM `vending_machines_info` AS vmi");
		sql.append(" LEFT JOIN `vending_line` AS vl ON vmi.`lineId` =  vl.`id`");
		sql.append(" WHERE vmi.`code` = '"+vmCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MachinesLAC mlac = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				mlac = new MachinesLAC();
				mlac.setAreaId(rs.getInt("areaId"));
				mlac.setCompanyId(rs.getInt("companyId"));
				mlac.setLineId(rs.getInt("lineId"));
				mlac.setVmCode(vmCode);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VendingMachinesInfoDaoImpl--getMachinesLAC--end>");
		return mlac;
	}

    @Override
    public List<VendingMachinesInfoBean> findVendingMachinesByCodes( List<String> codes ) {
        String vmCodes = String.join(",",codes);
        StringBuffer sql = new StringBuffer();
        sql.append ( " select startUsingTime,c.name as companyName,banWayNumber,code,companyId,errorWarn,isShowH5,itemType,lat,linkman,"
                + " locatoinDate,locatoinName,lon,machinesBaseId,vmi.name,respManId,vmi.state,vmi.areaId,machineVersion "
                + " from vending_machines_info vmi left join company c on c.id = vmi.companyId where  code in (" + vmCodes );
        sql.append ( ") and companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
        log.info("根据售卖机code查询售卖机信息--sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<VendingMachinesInfoBean> list=new ArrayList<>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                VendingMachinesInfoBean vmInfoBean = new VendingMachinesInfoBean();
                vmInfoBean.setBanWayNumber(rs.getString("banWayNumber"));
                vmInfoBean.setCode(rs.getString("code"));
                vmInfoBean.setCompanyId(rs.getInt("companyId"));
                vmInfoBean.setErrorWarn(rs.getInt("errorWarn"));
                vmInfoBean.setIsShowH5(rs.getInt("isShowH5"));
                vmInfoBean.setItemType(rs.getString("itemType"));
                vmInfoBean.setLat(rs.getDouble("lat"));
                vmInfoBean.setLinkman(rs.getString("linkman"));
                vmInfoBean.setLocatoinDate(rs.getDate("locatoinDate"));
                vmInfoBean.setLocatoinName(rs.getString("locatoinName"));
                vmInfoBean.setLon(rs.getDouble("lon"));
                vmInfoBean.setMachinesBaseId(rs.getInt("machinesBaseId"));
                vmInfoBean.setName(rs.getString("name"));
                vmInfoBean.setRespManId(rs.getLong("respManId"));
                vmInfoBean.setState(rs.getInt("state"));
                String stateName = StateInfoDaoImpl.getNameByState(rs.getLong("state"));
                if (stateName != null) {
                    vmInfoBean.setStateName(stateName);
                }
                vmInfoBean.setAreaId(rs.getInt("areaId"));
                vmInfoBean.setMachineVersion(rs.getInt("machineVersion"));
                list.add(vmInfoBean);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeConnection(rs, ps, conn);
        }
        // log.info("<VendingMachinesInfoDaoImpl--findVendingMachinesByCode--end>");
        return list;
    }


    @Override
	public VmbaseInfoDto getBaseInfo(String vmCode) {
		log.info("<VendingMachinesInfoDaoImpl--getBaseInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmb.factoryNumber,vmb.aisleConfiguration AS totalWayNum,vmi.`machineVersion`");
		sql.append(" FROM vending_machines_info AS vmi ");
		sql.append(" INNER JOIN vending_machines_base AS vmb ON vmi.`machinesBaseId` = vmb.`id`");
		sql.append(" WHERE vmi.`code` = '"+vmCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VmbaseInfoDto baseInfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				baseInfo = new VmbaseInfoDto();
				baseInfo.setFactoryNumber(rs.getString("factoryNumber"));
				baseInfo.setMachinesVersion(rs.getInt("machineVersion"));
				baseInfo.setTotalWayNum(rs.getInt("totalWayNum"));
				baseInfo.setVmCode(vmCode);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VendingMachinesInfoDaoImpl--getBaseInfo--end>");
		return baseInfo;
	}
	
}
