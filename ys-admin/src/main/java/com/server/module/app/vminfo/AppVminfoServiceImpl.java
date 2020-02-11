package com.server.module.app.vminfo;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.server.module.app.replenish.ReplenishForm;


@Service("aliVminfoService")
public class AppVminfoServiceImpl implements AppVminfoService{

	public static Logger log = LogManager.getLogger(AppVminfoServiceImpl.class); 
	@Autowired
	@Qualifier("aliVminfoDao")
	private AppVminfoDao vminfoDao;
	
	@Override
	public List<VminfoDto> queryVMList(String condition, String companyIds, Integer machineType ,Integer state) {
		log.info("<AliVminfoServiceImpl--queryVMList--start>");
		List<VminfoDto> queryVMList = vminfoDao.queryVMList1(condition, companyIds, machineType,state);
		log.info("<AliVminfoServiceImpl--queryVMList--end>");
		return queryVMList;
	}
	@Override
	public List<VminfoDto> queryVMListForReplenishMan(String condition,String companyIds,Integer state) {
		log.info("<AliVminfoServiceImpl--queryVMListForReplenishMan--start>");
		List<VminfoDto> queryVMList = vminfoDao.queryVMListForReplenishMan(condition,companyIds,state);
		log.info("<AliVminfoServiceImpl--queryVMListForReplenishMan--end>");
		return queryVMList;
	}
	
	@Override
	public List<VminfoDto> queryOwnVMList(String condition,  Long dutyId, Integer machineType,Integer state) {
		log.info("<AliVminfoServiceImpl--queryOwnVMList--start>");
		List<VminfoDto> queryOwnVMList = vminfoDao.queryOwnVMList(condition, dutyId, machineType,state);
		log.info("<AliVminfoServiceImpl--queryOwnVMList--end>");
		return queryOwnVMList;
	}

	@Override
	public List<VminfoDto> queryReplenishVMList(ReplenishForm replenishForm) {
		log.info("<AliVminfoServiceImpl--queryReplenishVMList--start>");
		List<VminfoDto> queryReplenishVMList = vminfoDao.queryReplenishVMList(replenishForm);
		log.info("<AliVminfoServiceImpl--queryReplenishVMList--end>");
		return queryReplenishVMList;
	}

	@Override
	public Integer queryAllMachinesNum(Integer companyId, String companyIds,Integer state) {
		log.info("<AliVminfoServiceImpl--queryAllMachinesNum--start>");
		Integer queryAllMachinesNum = vminfoDao.queryAllMachinesNum(companyId, companyIds,state);
		log.info("<AliVminfoServiceImpl--queryAllMachinesNum--end>");
		return queryAllMachinesNum;
	}
	
	@Override
	public Integer queryAllMachinesNumForReplenishMan(Integer companyId, String companyIds,Integer state) {
		log.info("<AliVminfoServiceImpl--queryAllMachinesNumForReplenishMan--start>");
		Integer queryAllMachinesNum = vminfoDao.queryAllMachinesNumForReplenishMan(companyId, companyIds,state);
		log.info("<AliVminfoServiceImpl--queryAllMachinesNumForReplenishMan--end>");
		return queryAllMachinesNum;
	}

	@Override
	public VminfoDto queryByVmCode(String vmCode) {
		log.info("<AliVminfoServiceImpl--queryByVmCode--start>");
		VminfoDto queryByVmCode = vminfoDao.queryByVmCode(vmCode);
		log.info("<AliVminfoServiceImpl--queryByVmCode--end>");
		return queryByVmCode;
	}

	@Override
	public List<WayDto1> queryWayItem(List<String> listCode,int version) {
		return vminfoDao.queryWayItem(listCode,version);
	}
}
