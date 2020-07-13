package com.server.module.system.replenishManage.machinesReplenishStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-04-24 11:53:01
 */
@Service
public class ReplenishmentReportServiceImpl implements ReplenishmentReportService {

	public static Logger log = LogManager.getLogger(ReplenishmentReportServiceImpl.class); 
	@Autowired
	private ReplenishmentReportDao replenishmentReportDaoImpl;

	/**
	 * 补货报表列表查询
	 */
	public ReturnDataUtil listPage(ReplenishmentReportForm replenishmentReportForm) {
		return replenishmentReportDaoImpl.listPage(replenishmentReportForm);
	}

	/**
	 * 补货报表列表查询
	 */
	public ReturnDataUtil visionListPage(ReplenishmentReportForm replenishmentReportForm) {
		return replenishmentReportDaoImpl.visionListPage(replenishmentReportForm);
	}
	
	/**
	 * 补货报表列表查询
	 */
	public ReturnDataUtil changeListPage(ReplenishmentReportForm replenishmentReportForm) {
		return replenishmentReportDaoImpl.changeListPage(replenishmentReportForm);
	}

	@Override
	public ReturnDataUtil userListPage(ReplenishmentReportForm replenishmentReportForm) {
		return replenishmentReportDaoImpl.userListPage(replenishmentReportForm);
	}
	
	/**
	 * 补货人员详情列表查询
	 */
	@Override
	public ReturnDataUtil userListPageDetail(ReplenishmentReportForm replenishmentReportForm) {
		return replenishmentReportDaoImpl.userlistPageDetail(replenishmentReportForm);
	}
}
