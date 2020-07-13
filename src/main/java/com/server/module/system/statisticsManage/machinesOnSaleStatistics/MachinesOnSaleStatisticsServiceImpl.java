package com.server.module.system.statisticsManage.machinesOnSaleStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.common.persistence.Page;
import com.server.util.ReturnDataUtil;


@Service("machinesOnSaleStatisticsService")
public class MachinesOnSaleStatisticsServiceImpl implements MachinesOnSaleStatisticsService {

	@Autowired
	MachinesOnSaleStatisticsDao machinesOnStatisticsDao;

	/**
	 * 统计在售商品信息
	 * @param machinesOnSaleStatisticsForm
	 * @return ReturnDataUtil
	 */
	@Override
	public ReturnDataUtil machinesOnSaleList(MachinesOnSaleStatisticsForm machinesOnSaleStatisticsForm) {
		return machinesOnStatisticsDao.machinesOnSaleList(machinesOnSaleStatisticsForm);
	}
	
	/**
	 * 在售商品信息详情
	 * @param machinesOnSaleStatisticsForm
	 * @return List<machinesOnSaleDto>
	 */
	@Override
	public ReturnDataUtil machinesOnSaleListDetail(MachinesOnSaleStatisticsForm machinesOnSaleStatisticsForm) {
		return machinesOnStatisticsDao.machinesOnSaleListDetail(machinesOnSaleStatisticsForm);

	}


}
