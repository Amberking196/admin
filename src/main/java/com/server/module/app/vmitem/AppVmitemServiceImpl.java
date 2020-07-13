package com.server.module.app.vmitem;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.server.module.system.statisticsManage.payRecord.ListSaleNumCondition;
import com.server.module.system.statisticsManage.payRecord.ListSaleNumDTO;
import com.server.module.system.statisticsManage.payRecord.PayRecordDao;
import com.server.util.DateUtil;
import com.server.util.JsonUtils;
import com.server.util.ReturnDataUtil;

@Service("aliVmitemService")
public class AppVmitemServiceImpl implements AppVmitemService {

	public static Logger log = LogManager.getLogger(AppVmitemServiceImpl.class);
	@Autowired
	@Qualifier("aliVmitemDao")
	private AppVmitemDao vmitemDao;
	@Autowired
	private PayRecordDao payRecordDaoImpl;

	@Override
	public Long queryBasicItemId(String vmCode, Integer wayNum) {
		log.info("<AliVmitemServiceImpl--queryBasicItemId--start>");
		Long queryBasicItemId = vmitemDao.queryBasicItemId(vmCode, wayNum);
		log.info("<AliVmitemServiceImpl--queryBasicItemId--end>");
		return queryBasicItemId;
	}

	@Override
	public List<ItemDto> queryVmitem(String vmCode, int machineVersion) {
		List<ItemDto> queryVmitem = null;
		if (machineVersion == 1) {
			queryVmitem = vmitemDao.queryVmitem(vmCode);
		} else {
			queryVmitem = vmitemDao.queryVmItem2(vmCode);
		}
		if (queryVmitem != null && queryVmitem.size() > 0) {// 计算最优货道容量推荐值
			int size = queryVmitem.size();
			// 查询售货机一个月所有商品的销量总和
			ListSaleNumCondition condition = new ListSaleNumCondition();
			condition.setVmCode(vmCode);
			Date date = new Date();
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.add(Calendar.MONTH, -1);
			Date dt1 = rightNow.getTime();
			String startTime = DateUtil.formatYYYYMMDD(dt1);
			condition.setStartTime(startTime);
			condition.setEndTime(DateUtil.formatYYYYMMDD(date));
			ReturnDataUtil returnData = payRecordDaoImpl.listSaleNum(condition);
			@SuppressWarnings("unchecked")
			List<ListSaleNumDTO> list1 = (List<ListSaleNumDTO>) returnData.getReturnObject();
			// 商品的销量总和
			int coutSalesVolume = 0;
			for (ListSaleNumDTO listSaleNumDTO : list1) {
				coutSalesVolume += listSaleNumDTO.getNum();
			}
			log.info("商品的销量总和==" + coutSalesVolume);
			log.info(JsonUtils.toJson(list1));
			log.info(JsonUtils.toJson(queryVmitem));
			for (ListSaleNumDTO listSaleNumDTO : list1) {
				for (ItemDto wayDto : queryVmitem) {
					// 判断货道是否相等
					if (listSaleNumDTO.getWayNumber() == wayDto.getDoorNO() && 
							listSaleNumDTO.getBasicItemId().equals(wayDto.getBasicItemId())) {
						log.info(wayDto.getDoorNO());
						// 计算最优货道容量推荐值
						BigDecimal a = new BigDecimal(
								(listSaleNumDTO.getNum() * wayDto.getFullNum().intValue() * size));
						BigDecimal b = new BigDecimal(coutSalesVolume);
						BigDecimal newNumber = a.divide(b, 0, BigDecimal.ROUND_HALF_UP);
						wayDto.setRecommendCapacity(newNumber.intValue());
						break;
					}
				}
			}
		}
		return queryVmitem;
	}
	
}
