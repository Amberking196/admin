package com.server.module.system.statisticsManage.merchandiseSalesStatistics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-05-09 21:15:27
 */
@Service
public class MerchandiseSalesStatisticsServiceImpl implements MerchandiseSalesStatisticsService {

	public static Logger log = LogManager.getLogger(MerchandiseSalesStatisticsServiceImpl.class); 
	@Autowired
	private MerchandiseSalesStatisticsDao merchandiseSalesStatisticsDaoImpl;

	/**
	 * 商品销售统计列表查询
	 */
	public ReturnDataUtil listPage(MerchandiseSalesStatisticsCondition condition) {
		log.info("<MerchandiseSalesStatisticsServiceImpl>----<listPage>----start");
		ReturnDataUtil listPage = merchandiseSalesStatisticsDaoImpl.listPage(condition);
		log.info("<MerchandiseSalesStatisticsServiceImpl>----<listPage>----end");
		return listPage;
	}

	

	
}
