package com.server.module.system.statisticsManage.notSaleMachines;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@Service
public class NotSaleMachinesServiceImpl implements NotSaleMachinesService{

	private static Logger log = LogManager.getLogger(NotSaleMachinesServiceImpl.class);

	@Autowired
	private NotSaleMachinesDao notSaleMachinesDao;
	@Override
	public ReturnDataUtil findNotSaleMachinesByForm(NotSaleMachinesForm form) {
		log.info("<NotSaleMachinesServiceImpl--findNotSaleMachinesByForm--start>");
		List<NotSaleMachinesDto> notSaleList = notSaleMachinesDao.findNotSaleMachinesByForm(form);
		Long total = notSaleMachinesDao.findNumOfNotSaleMachinesByForm(form);
		log.info("<NotSaleMachinesServiceImpl--findNotSaleMachinesByForm--end>");
		if(notSaleList!=null && notSaleList.size()>0){
			return ResultUtil.success(notSaleList,form.getCurrentPage(),total);
		}
		return ResultUtil.error();
	}
}
