package com.server.common.task;

import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.server.module.system.itemManage.TblStatisticsItemSale.TblStatisticsItemSaleService;
import com.server.util.DateUtil;


@Component
public class ItemSaleTask {
	
	private Logger log = Logger.getLogger(ItemSaleTask.class);    
	
	
	@Autowired
	private TblStatisticsItemSaleService  TblStatisticsItemSaleServiceImpl;
	
	 
	
	//@Scheduled(cron = "0 */1 * * * ? ")  //隔一分钟执行一次
	
	//@Scheduled(cron=" 0 12 0 * *  ? ") //每天 晚上 凌晨12点12分执行一次
	public void createReport() {
		log.info("<ItemSaleTask>------start--->>>>"+DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		TblStatisticsItemSaleServiceImpl.add();
		/*List<TblStatisticsItemSaleBean> list = TblStatisticsItemSaleServiceImpl.getItemSaleBean();
		for (TblStatisticsItemSaleBean bean : list) {
			log.info(bean.getItemOnVmNum()+">>>>>>>>>>>>>>>>>>"+bean.getBasicItemId());
		}
		log.info("查询结果>>>>"+list.size());*/
		log.info("<ItemSaleTask>------end--->>>>"+DateUtil.formatYYYYMMDDHHMMSS(new Date()));
	}

}
