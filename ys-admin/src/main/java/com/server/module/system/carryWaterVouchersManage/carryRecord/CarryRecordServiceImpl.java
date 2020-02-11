package com.server.module.system.carryWaterVouchersManage.carryRecord;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

@Service
public class CarryRecordServiceImpl implements CarryRecordService{
	
	private final static Logger log = LogManager.getLogger(CarryRecordDaoImpl.class);

	@Autowired
	private CarryRecordDao carryRecordDao;

	/**
	 * 查询订单的提水券使用情况
	 * @author hjc
	 * @date 2018年11月9日上午9:42:45
	 * @param carryRecordForm
	 * @return
	 */
	@Override
	public ReturnDataUtil listPage(CarryRecordForm carryRecordForm){
		log.info("<CarryRecordServiceImpl--listPage--start>");
		ReturnDataUtil returnDataUtil = carryRecordDao.listPage(carryRecordForm);
		log.info("<CarryRecordServiceImpl--listPage--end>");
		return returnDataUtil;
	}

}
