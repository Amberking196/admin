package com.server.module.system.machineManage.machinesTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.commonBean.TotalResultBean;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;


@Service
public class MachinesTestLogServiceImpl implements MachinesTestLogService{
	
	private final static Logger log = LogManager.getLogger(MachinesTestLogServiceImpl.class);
	
	@Autowired
	private MachinesTestLogDao machinesTestLog;

	@Override
	public ReturnDataUtil getMachinesTest(MachinesTestForm form) {
		log.info("MachinesTestLogServiceImpl--getMachinesTest--start");
		TotalResultBean<List<MachinesTestLogDto>> machinesTest = machinesTestLog.getMachinesTest(form);
		log.info("MachinesTestLogServiceImpl--getMachinesTest--end");
		return ResultUtil.success(machinesTest.getResult(), form.getCurrentPage(), machinesTest.getTotal());
	}
	
	@Override
	public ReturnDataUtil geteTestRecord(Long id) {
		log.info("MachinesTestLogServiceImpl--geteTestRecord--start");
		Map<String,List<MachinesTestResultDto>> testRecordMap = new HashMap<String,List<MachinesTestResultDto>>();
		MachinesTestLogBean testLog = machinesTestLog.getTestLog(id);
		if(testLog != null){
			testRecordMap.put("purchaseTestResult", machinesTestLog.getPurchaseTestResult(testLog));
			testRecordMap.put("replenishTestResult", machinesTestLog.getReplenishTestResult(testLog));
		}
		log.info("MachinesTestLogServiceImpl--geteTestRecord--end");
		if(testRecordMap.size()>0){
			return ResultUtil.success(testRecordMap);
		}
		return ResultUtil.selectError();
	}
}
