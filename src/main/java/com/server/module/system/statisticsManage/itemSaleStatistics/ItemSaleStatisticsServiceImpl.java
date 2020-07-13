package com.server.module.system.statisticsManage.itemSaleStatistics;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.commonBean.IdNameBean;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.companyManage.CompanyService;
import com.server.util.ReturnDataUtil;
@Service("itemSaleStatisticsService")
public class ItemSaleStatisticsServiceImpl implements ItemSaleStatisticsService {

	@Autowired
	ItemSaleStatisticsDao itemStatisticsDao;
	@Override
	public ReturnDataUtil findItemSaleStatistics(ItemSaleStatisticsForm itemSaleStatisticsForm) {
		ReturnDataUtil returnData = new ReturnDataUtil();
//		List<Map<String,Object>> machinesNumList = itemStatisticsDao.findItemPutMachinesNum(itemSaleStatisticsForm);
		List<ItemSaleStatisticsDto> itemDtoList = itemStatisticsDao.findItemSaleStatisticsAfterRefund(itemSaleStatisticsForm);
//		Long basicItemId = null;
//		for (ItemSaleStatisticsDto itemDto : itemDtoList) {
//			basicItemId = itemDto.getBasicItemId();
//			if(basicItemId!=null){
//				for (Map<String,Object> map : machinesNumList) {
//					if(map.get("basicItemId").equals(basicItemId)){
//						itemDto.setMachinesNum(map.get("machinesNum").toString());
//					}
//				}
//			}
//		}
		returnData.setStatus(1);
		returnData.setMessage("查询成功");
		returnData.setCurrentPage(itemSaleStatisticsForm.getCurrentPage());
		long totalNum = itemStatisticsDao.findItemSalesStatisticsNum(itemSaleStatisticsForm);
		returnData.setTotal(totalNum);
		int totalPage = (int)(totalNum%itemSaleStatisticsForm.getPageSize()==0?totalNum/itemSaleStatisticsForm.getPageSize():totalNum/itemSaleStatisticsForm.getPageSize()+1);
		returnData.setTotalPage(totalPage);
		returnData.setReturnObject(itemDtoList);
		return returnData;
		
	}
	@Override
	public List<IdNameBean> findItemInfo() {
		return itemStatisticsDao.findItemInfo();
	}
	
	@Override
	public List<IdNameBean> findItemInfoByCompanyId(ItemSaleStatisticsForm itemSaleStatisticsForm){
		return itemStatisticsDao.findItemInfoByCompanyId(itemSaleStatisticsForm);
	}
}
