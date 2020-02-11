package com.server.module.system.statisticsManage.payRecord;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.module.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.module.system.userManage.CustomerService;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.ItemTypeEnum;
import com.server.util.stateEnum.PayStateEnum;
import com.server.util.stateEnum.PayTypeEnum;

@Service("payRecordService")
public class PayRecordServiceImpl implements PayRecordService {

	@Autowired
	PayRecordDao payRecordDao;

	@Override
	public ReturnDataUtil findPayRecord(PayRecordForm payRecordForm) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<PayRecordDto> payDtoList = payRecordDao.findPayRecord(payRecordForm);
		SumPayRecordDto sumPayReocrd = payRecordDao.findPayRecordNum(payRecordForm);
		//查询支付失败总价
		Double failurePrice = payRecordDao.findFailurePrice(payRecordForm);
		if (failurePrice != null) {
			sumPayReocrd.setFailurePrice(failurePrice);
		}
		returnData.setCurrentPage(payRecordForm.getCurrentPage());
		int totalPage = (int) (sumPayReocrd.getTotal() % payRecordForm.getPageSize() == 0 ? sumPayReocrd.getTotal() / payRecordForm.getPageSize() : sumPayReocrd.getTotal() / payRecordForm.getPageSize() + 1);
		returnData.setTotalPage(totalPage);
		for (PayRecordDto payRecordDto : payDtoList) {
			payRecordDto.setItemType(ItemTypeEnum.getItemTypeInfo(payRecordDto.getItemTypeId()));
			payRecordDto.setPayType(PayTypeEnum.getPayTypeInfo(payRecordDto.getPayTypeId()));
			payRecordDto.setState(PayStateEnum.findStateName(payRecordDto.getStateId()));

			if(StringUtils.isNotBlank(payRecordDto.getState()) && payRecordDto.getState().equals("空白订单")) {
				payRecordDto.setState("未购买");
			}
		}
		if (payDtoList != null && payDtoList.size() > 0 && sumPayReocrd != null) {
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			returnData.setTotal(sumPayReocrd.getTotal());
			Map<String, Object> result = new HashMap<String, Object>();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.add(result);
			result.put("payDtoList", payDtoList);
			result.put("sumPayReocrd", sumPayReocrd);
			returnData.setReturnObject(list);
		} else {
			returnData.setStatus(0);
			returnData.setMessage("查询失败");
		}
		return returnData;
	}

	
	@Override
	public ReturnDataUtil findVisionPayRecord(PayRecordForm payRecordForm) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<PayRecordDto> payDtoList = payRecordDao.findVisionPayRecord(payRecordForm);
		SumPayRecordDto sumPayReocrd = payRecordDao.findVisionPayRecordNum(payRecordForm);
		//查询支付失败总价
		Double failurePrice = payRecordDao.findVisionFailurePrice(payRecordForm);
		if (failurePrice != null) {
			sumPayReocrd.setFailurePrice(failurePrice);
		}
		returnData.setCurrentPage(payRecordForm.getCurrentPage());
		int totalPage = (int) (sumPayReocrd.getTotal() % payRecordForm.getPageSize() == 0 ? sumPayReocrd.getTotal() / payRecordForm.getPageSize() : sumPayReocrd.getTotal() / payRecordForm.getPageSize() + 1);
		returnData.setTotalPage(totalPage);
		for (PayRecordDto payRecordDto : payDtoList) {
			payRecordDto.setItemType(ItemTypeEnum.getItemTypeInfo(payRecordDto.getItemTypeId()));
			payRecordDto.setPayType(PayTypeEnum.getPayTypeInfo(payRecordDto.getPayTypeId()));
			payRecordDto.setState(PayStateEnum.findStateName(payRecordDto.getStateId()));

			if(StringUtils.isNotBlank(payRecordDto.getState()) && payRecordDto.getState().equals("空白订单")) {
				payRecordDto.setState("未购买");
			}
		}
		if (payDtoList != null && payDtoList.size() > 0 && sumPayReocrd != null) {
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			returnData.setTotal(sumPayReocrd.getTotal());
			Map<String, Object> result = new HashMap<String, Object>();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.add(result);
			result.put("payDtoList", payDtoList);
			result.put("sumPayReocrd", sumPayReocrd);
			returnData.setReturnObject(list);
		} else {
			returnData.setStatus(0);
			returnData.setMessage("查询失败");
		}
		return returnData;
	}

	@Override
	public ReturnDataUtil deletePayRecord(List<Integer> payRecordIds) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if (payRecordIds != null && payRecordIds.size() > 0 && payRecordDao.deletePayRecord(payRecordIds)) {
			returnData.setStatus(1);
			returnData.setMessage("删除成功");
		} else {
			returnData.setStatus(0);
			returnData.setMessage("删除失败");
		}
		return returnData;
	}

	@Override
	public ReturnDataUtil updateOrderState(Integer state, Integer id,Integer orderType) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if (id != null && state != null && payRecordDao.updateOrderState(state,id,orderType)) {
			returnData.setStatus(1);
			returnData.setMessage("更新状态成功");
		} else {
			returnData.setStatus(0);
			returnData.setMessage("更新状态失败");
		}
		return returnData;
	}

	/**
	 * 统计销售量    公司   区域   线路
	 */
	public ReturnDataUtil listSellNumStatisticsPage(PayRecordForm1 condition) {
		return payRecordDao.listSellNumStatisticsPage(condition);
	}

	@Override
	public ReturnDataUtil findMachineHistory(CustomerMachineForm customerMachineForm) {
		WeightNumDto wnInfo = payRecordDao.getOrderWeightNum(customerMachineForm.getPayCode());
		if(wnInfo != null){
			Integer theroticalValue = 0 ;
			for (NumDto numInfo : wnInfo.getNumList()) {
				payRecordDao.getOrderNumChange(numInfo, wnInfo.getVmCode(), wnInfo.getWayNumber(), wnInfo.getCreateOrderTime());
				theroticalValue += numInfo.getBuyNum()*numInfo.getUnitWeight();
			}
			wnInfo.setTheroticalValue(theroticalValue);
			return ResultUtil.success(wnInfo);
		}
		return ResultUtil.error();
	}

	@Override
	public ReturnDataUtil findMachineHistoryVision(CustomerMachineForm customerMachineForm) {
		WeightNumDto wnInfo = payRecordDao.getOrderWeightNumVision(customerMachineForm.getPayCode());
		if(wnInfo != null){
			Integer theroticalValue = 0 ;
			for (NumDto numInfo : wnInfo.getNumList()) {
				payRecordDao.getOrderNumChangeVision(numInfo, wnInfo.getVmCode(), wnInfo.getWayNumber(), wnInfo.getCreateOrderTime());
				theroticalValue += numInfo.getBuyNum()*numInfo.getUnitWeight();
			}
			wnInfo.setTheroticalValue(theroticalValue);
			return ResultUtil.success(wnInfo);
		}
		return ResultUtil.error();
	}

	/**
	 * 最近七天销售记录
	 *
	 * @return
	 */
	public Map<String, Object> payBefore7Day(String companyId) {
		//select SUM(price) AS jiner from pay_record l inner join vending_machines_info m on l.vendingMachinesCode=m.code where m.companyId=76
		String companySqlStr = "";

		/*if(StringUtils.isNotBlank(companyId)){
			companySqlStr="  companyId="+companyId;
		}else{
			Integer id=UserUtils.getUser().getCompanyId();
			if(id!=1)
			companySqlStr="  companyId="+id;

		}
        if(!companySqlStr.equals(""))
		companySqlStr=companySqlStr+" and ";*/


		Map<String, Object> map = Maps.newHashMap();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		DateTime dt1 = new DateTime();
		String before7Day = sf.format(dt1.minusDays(6).withTime(0, 0, 0, 0).getMillis());

		List<Map<String, Object>> listday = payRecordDao.getBefore7Day(companySqlStr, before7Day);
		Integer[] days = new Integer[listday.size()];
		BigDecimal[] jiners = new BigDecimal[listday.size()];
		for (int i = 0; i < listday.size(); i++) {
			Map<String, Object> mapday = listday.get(i);
			days[i] = (Integer) mapday.get("day");
			jiners[i] = (BigDecimal) mapday.get("jiner");
		}
		DateTime today = new DateTime();
		String todayStr = sf.format(today.withTime(0, 0, 0, 0).getMillis());

		List<Map<String, Object>> listToday = payRecordDao.getToday(companySqlStr, todayStr);
		BigDecimal todayJiner = new BigDecimal(0d);
		if (listToday.size() > 0) {
			Map<String, Object> map1 = listToday.get(0);
			todayJiner = (BigDecimal) map1.get("jiner");
			if (todayJiner == null)
				todayJiner = new BigDecimal(0d);
		}

		DateTime yesterday = new DateTime();
		String yesterdayStr = sf.format(today.minusDays(1).withTime(0, 0, 0, 0).getMillis());
		System.out.println(yesterdayStr);

		List<Map<String, Object>> listYesterday = payRecordDao.getYesterday(companySqlStr, yesterdayStr);


		BigDecimal yesterdayJiner = new BigDecimal(0d);
		if (listYesterday.size() > 0) {
			Map<String, Object> map1 = listYesterday.get(0);
			yesterdayJiner = (BigDecimal) map1.get("jiner");
			if (yesterdayJiner == null)
				yesterdayJiner = new BigDecimal(0);
			yesterdayJiner = yesterdayJiner.subtract(todayJiner);
		}
		DateTime month = new DateTime();
		String monthStr = sf.format(today.withDayOfMonth(1).withTime(0, 0, 0, 0).getMillis());
		System.out.println(monthStr);

		List<Map<String, Object>> listMonth = payRecordDao.getMonth(companySqlStr, monthStr);
		BigDecimal monthJiner = new BigDecimal(0);
		if (listMonth.size() > 0) {
			Map<String, Object> map1 = listMonth.get(0);
			monthJiner = (BigDecimal) map1.get("jiner");
		}

		map.put("today", todayJiner);
		map.put("yesterday", yesterdayJiner);
		map.put("month", monthJiner);
		map.put("days", days);
		map.put("jiners", jiners);
		return map;
	}

	@Override
	public ReturnDataUtil payRecordDetail(PayRecordForm payRecordForm) {
		return payRecordDao.payRecordDetail(payRecordForm);
	}

	@Override
	public ReturnDataUtil findPayRecordForExport(PayRecordForm payRecordForm) {
		ReturnDataUtil returnData = payRecordDao.findPayRecordForExport(payRecordForm);
		List<PayRecordDto> data = (List<PayRecordDto>) returnData.getReturnObject();
		return returnData;
	}

	public ReturnDataUtil listSaleNum(ListSaleNumCondition condition) {
		ReturnDataUtil returnData = payRecordDao.listSaleNum(condition);
        List<ListSaleNumDTO> list = (List<ListSaleNumDTO>)returnData.getReturnObject();
        List<SaleNumDTO> list1 = Lists.newArrayList();
        int len = list.size();
        if(len>0){
			SaleNumDTO dto = new SaleNumDTO();
			dto.setVmCode(list.get(0).getVmCode());
			list1.add(dto);
		}

		for (int i = 0; i < len; i++) {
			ListSaleNumDTO obj = list.get(i);
			SaleNumDTO dto = list1.get(list1.size()-1);
			if(obj.getVmCode().equals(dto.getVmCode())){
				SaleNumItemDTO saleItem = genSaleNumItemDTO(obj);
				dto.getList().add(saleItem);
			} else {//
				SaleNumDTO temp = new SaleNumDTO();
				temp.setVmCode(obj.getVmCode());
				SaleNumItemDTO saleItem = genSaleNumItemDTO(obj);
				temp.getList().add(saleItem);
				list1.add(temp);
			}
		}

		returnData.setReturnObject(list1);
		return returnData;
	}

	private SaleNumItemDTO genSaleNumItemDTO(ListSaleNumDTO obj) {
		SaleNumItemDTO saleItem = new SaleNumItemDTO();
		saleItem.setItemName(obj.getItemName());
		saleItem.setWayNumber(obj.getWayNumber());
		saleItem.setNum(obj.getNum());
		return saleItem;
	}

	@Override
	public List<PayRecordItemDto> getPayRecordItemList(String payCode) {
		
		return payRecordDao.getPayRecordItemList(payCode);
	}

}
