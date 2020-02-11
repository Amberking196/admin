package com.server.module.system.statisticsManage.machinesSaleStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.common.persistence.Page;
import com.server.util.ReturnDataUtil;


@Service("machinesSaleStatisticsService")
public class MachinesSaleStatisticsServiceImpl implements MachinesSaleStatisticsService {

	@Autowired
	MachinesSaleStatisticsDao machinesStatisticsDao;
	@Override
	public ReturnDataUtil findPerMachinesSale(MachinesSaleStatisticsForm machinesStatisticsForm) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<PerMachinesSaleDto> perMachinesSaleDtoList = machinesStatisticsDao.findPerMachinesSale(machinesStatisticsForm);

		long total = machinesStatisticsDao.findMachinesSaleNumRecord(machinesStatisticsForm);
		returnData.setCurrentPage(machinesStatisticsForm.getCurrentPage());
		returnData.setStatus(1);
		returnData.setMessage("查询成功");
		returnData.setTotal(total);
		int totalPage = (int)(total%machinesStatisticsForm.getPageSize()==0?total/machinesStatisticsForm.getPageSize():total/machinesStatisticsForm.getPageSize()+1);
		returnData.setTotalPage(totalPage);
		
		returnData.setReturnObject(perMachinesSaleDtoList);
		
		
		return returnData;
	}
	
	@Override
	public ReturnDataUtil findPerMachinesSaleDetail(MachinesSaleStatisticsForm machinesStatisticsForm) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<PerMachinesSaleDto> perMachinesSaleDtoList = machinesStatisticsDao.findPerMachinesSaleDetail(machinesStatisticsForm);

		returnData.setCurrentPage(machinesStatisticsForm.getCurrentPage());
		returnData.setStatus(1);
		returnData.setMessage("查询成功");
		returnData.setReturnObject(perMachinesSaleDtoList);
		return returnData;
	}
	
	@Override
	public ReturnDataUtil findPerMachinesCustomerConsume(MachinesSaleStatisticsForm machinesStatisticsForm) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData = machinesStatisticsDao.findPerMachinesCustomerConsume(machinesStatisticsForm);
		return returnData;
	}


	@Override
	public ReturnDataUtil findSumMachinesSale(MachinesSaleStatisticsForm machinesStatisticsForm) {
		SumMachinesSaleDto sumMachinesSaleDtoList = machinesStatisticsDao.findSumMachinesSale(machinesStatisticsForm);
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(sumMachinesSaleDtoList!=null) {
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			returnData.setReturnObject(sumMachinesSaleDtoList);
		}
		return returnData;
	}

}
