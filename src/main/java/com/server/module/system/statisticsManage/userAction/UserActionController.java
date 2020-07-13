package com.server.module.system.statisticsManage.userAction;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.statisticsManage.chart.ChartForm;
import com.server.module.system.statisticsManage.chart.ChartUtil;
import com.server.module.system.statisticsManage.chart.DateCountVo;
import com.server.module.system.statisticsManage.chart.Grouping;
import com.server.module.system.statisticsManage.chart.ResponseData;
import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayDto;
import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayForm;
import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayService;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ReturnDataUtil;

import net.bytebuddy.asm.Advice.Return;


@Controller
@RequestMapping("/userAction")
public class UserActionController {


	@Autowired
	CompanyService companyService;
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	PayRecordPerDayService payRecordPerDayService;
	/**
	 * 用户购买情况/与图表
	 */
	@RequestMapping(value="/userBuyStationChart",method=RequestMethod.POST)
	@ResponseBody
	public Map<Object,Object> userBuyStationChart(@RequestBody(required=false) PayRecordPerDayForm payDayForm,HttpServletRequest request){
		if(payDayForm==null){
			payDayForm = new PayRecordPerDayForm();
		}
		if(payDayForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			payDayForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payDayForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		payDayForm.setCompanyIds(companyIds);
		payDayForm.setIsShowAll(1);
		List<PayRecordPerDayDto> list=payRecordPerDayService.userBuyStation(payDayForm);
		List<Date> x = new ArrayList<Date>();
		List<Double> y = new ArrayList<Double>();
		List<Double> z = new ArrayList<Double>();
		List<Double> h = new ArrayList<Double>();

		UserChart uc1=new UserChart();uc1.setUnit("台");uc1.setName("机器台数");
		UserChart uc2=new UserChart();uc2.setUnit("元");uc2.setName("销售金额");
		UserChart uc3=new UserChart();uc3.setUnit("元");uc3.setName("台均销售额");
		SumUserAction sua=new SumUserAction();

		for(PayRecordPerDayDto p:list) {
			p.setNormalNum(payRecordPerDayService.findNormalMachines(payDayForm,p.getCreateTime()));
			x.add(p.getCreateTime());
			y.add((double)p.getNormalNum());
			z.add(p.getFinishedMoney());
			if(p.getNormalNum()==0) {
				h.add(0d);
			}else {
				h.add((new BigDecimal(p.getFinishedMoney()).divide(new BigDecimal(p.getNormalNum()),2, BigDecimal.ROUND_HALF_UP).doubleValue()));
			}
			sua.setSumFinishedMoney(sua.getSumFinishedMoney().add(new BigDecimal(p.getFinishedMoney())));
			sua.setSumFinishedOrderNum(sua.getSumFinishedOrderNum().add(p.getFinishedOrderNum()));
			sua.setSumAverageMoney(sua.getSumAverageMoney().add(p.getAverageMoney()));
		};
		sua.setSumFinishedOrderNum(sua.getSumFinishedOrderNum().setScale(2, BigDecimal.ROUND_HALF_UP));
		sua.setSumAverageMoney(sua.getSumAverageMoney().divide(new BigDecimal(list.size()),2, BigDecimal.ROUND_HALF_UP));
		sua.setSumFinishedMoney(sua.getSumFinishedMoney().setScale(2, BigDecimal.ROUND_HALF_UP));

		uc1.setY(y);uc2.setY(z);uc3.setY(h);
		List<UserChart> u=new ArrayList<UserChart>();
		u.add(uc1);u.add(uc2);u.add(uc3);
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("x", x);
		map.put("list", u);
		map.put("sumFinishedOrderNum",sua.getSumFinishedOrderNum());
		map.put("sumAverageMoney",sua.getSumAverageMoney());
		map.put("sumFinishedMoney",sua.getSumFinishedMoney());

		return map;
	}
	
	/**
	 * 用户购买情况/与图表
	 */
	@RequestMapping(value="/userBuyStation",method=RequestMethod.POST)
	@ResponseBody
	public Map<Object,Object> userBuyStation(@RequestBody(required=false) PayRecordPerDayForm payDayForm,HttpServletRequest request){
		if(payDayForm==null){
			payDayForm = new PayRecordPerDayForm();
		}
		if(payDayForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			payDayForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payDayForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		payDayForm.setCompanyIds(companyIds);
		Integer isShowAll=0;
		Integer currentPage=1;
		if(payDayForm.getIsShowAll()==0) {
			isShowAll=payDayForm.getIsShowAll();
			currentPage=payDayForm.getCurrentPage();
		}
		payDayForm.setIsShowAll(1);
		List<PayRecordPerDayDto> list=payRecordPerDayService.userBuyStation(payDayForm);
		SumUserAction sua=sumUserAction(list);
		payDayForm.setIsShowAll(isShowAll);
		payDayForm.setCurrentPage(currentPage);
		ReturnDataUtil returnData=payRecordPerDayService.userBuyStationNum(payDayForm);
		list=(List<PayRecordPerDayDto>) returnData.getReturnObject();
		Long total=returnData.getTotal();
		Integer totalPage=returnData.getTotalPage();
		
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("sumFinishedMoney",sua.getSumFinishedMoney());
		map.put("sumMachinesNum",sua.getSumMachinesNum());
		map.put("sumFinishedItemNum",sua.getSumFinishedItemNum());
		map.put("sumFinishedOrderNum",sua.getSumFinishedOrderNum());
		map.put("sumAverageMoney",sua.getSumAverageMoney());
		map.put("sumAverage",sua.getSumAverage());
		map.put("sumNormalNum",sua.getSumNormalNum());
		map.put("averageMinPirce",sua.getAverageMinPirce());
		map.put("averageMaxPirce",sua.getAverageMaxPirce());
		map.put("total",total);
		map.put("currentPage", currentPage);
		map.put("totalPage",totalPage);
		map.put("list", list);

		return map;
	}
	
	@GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
	public void export(PayRecordPerDayForm payDayForm,HttpServletRequest request,HttpServletResponse response){
		if(payDayForm==null){
			payDayForm = new PayRecordPerDayForm();
		}
		if(payDayForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			payDayForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payDayForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		payDayForm.setCompanyIds(companyIds);
		payDayForm.setIsShowAll(1);
		List<PayRecordPerDayDto> list=payRecordPerDayService.userBuyStation(payDayForm);
		String title = "用户购买情况统计";
		StringBuffer date = new StringBuffer("起始--至今");
		if (payDayForm.getStartDate() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate()));
		}
		if (payDayForm.getEndDate() != null) {
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()));
		}
		SumUserAction sua=sumUserAction(list);		
		PayRecordPerDayDto p=new PayRecordPerDayDto();
		p.setNormalNum(sua.getSumNormalNum().intValue());
		p.setFinishedOrderNum(sua.getSumFinishedOrderNum());
		p.setFinishedItemNum(sua.getSumFinishedItemNum());
		p.setAverage(sua.getSumAverage().doubleValue());
		p.setFinishedMoney(sua.getSumFinishedMoney().doubleValue());
		p.setMaxPrice(sua.getAverageMaxPirce().doubleValue());
		p.setMinPrice(sua.getAverageMinPirce().doubleValue());
		p.setAverageMoney(sua.getSumAverageMoney());
		p.setDay("总计");
		list.add(p);
		//设置导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(payDayForm.getStartDate()!=null&&payDayForm.getEndDate()!=null) {
			//导出日志内容按时间格式导出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(payDayForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(payDayForm.getEndDate())+"的用户购买情况的数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出用户购买情况的数据");
		}
		String[] headers = new String[] { "day","normalNum","finishedOrderNum","finishedItemNum","average","finishedMoney",
				"maxPrice","minPrice", "averageMoney"};
		String[] column = new String[] { "日期","机器数量","订单数","商品数量","平均商品数","销售额","最高单价",
				"最低单价","客单价"};
		try {
			ExcelUtil.exportExcel(title.toString(), headers, column, response, list, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			e.printStackTrace();
		}
	}
	
	public SumUserAction sumUserAction(List<PayRecordPerDayDto> list) {
		
		SumUserAction sua=new SumUserAction();
		for(PayRecordPerDayDto p:list) {
			sua.setSumFinishedMoney(sua.getSumFinishedMoney().add(new BigDecimal(p.getFinishedMoney())));
			sua.setSumMachinesNum(sua.getSumMachinesNum().add(new BigDecimal(p.getMachinesNum())));
			sua.setSumFinishedItemNum(sua.getSumFinishedItemNum().add(p.getFinishedItemNum()));
			sua.setSumFinishedOrderNum(sua.getSumFinishedOrderNum().add(p.getFinishedOrderNum()));
			sua.setSumAverageMoney(sua.getSumAverageMoney().add(p.getAverageMoney()));
			sua.setSumAverage(sua.getSumAverage().add(new BigDecimal(p.getAverage())));
			sua.setSumNormalNum(sua.getSumNormalNum().add(new BigDecimal(p.getNormalNum())));
            if(new BigDecimal(p.getMinPrice()).compareTo(sua.getAverageMinPirce())<0)
            	sua.setAverageMinPirce(new BigDecimal(p.getMinPrice()));
            if(new BigDecimal(p.getMaxPrice()).compareTo(sua.getAverageMaxPirce())>0)
            	sua.setAverageMaxPirce(new BigDecimal(p.getMaxPrice()));
		}
		sua.setSumFinishedMoney(sua.getSumFinishedMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		sua.setSumMachinesNum(sua.getSumMachinesNum().setScale(2, BigDecimal.ROUND_HALF_UP));
		sua.setSumFinishedItemNum(sua.getSumFinishedItemNum().setScale(2, BigDecimal.ROUND_HALF_UP));
		sua.setSumFinishedOrderNum(sua.getSumFinishedOrderNum().setScale(2, BigDecimal.ROUND_HALF_UP));
		if(list.size()==0) {
			sua.setSumAverage(new BigDecimal(0));
			sua.setSumAverageMoney(new BigDecimal(0));
		}else {
			sua.setSumAverage(sua.getSumAverage().divide(new BigDecimal(list.size()),2, BigDecimal.ROUND_HALF_UP));
			sua.setSumAverageMoney(sua.getSumAverageMoney().divide(new BigDecimal(list.size()),2, BigDecimal.ROUND_HALF_UP));
		}
		sua.setSumNormalNum(sua.getSumNormalNum().setScale(2, BigDecimal.ROUND_HALF_UP));
		return sua;
	}
}
