package com.server.module.system.statisticsManage.userActiveDegree;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.commonBean.TotalResultBean;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@RestController
@RequestMapping("/userActive")
public class UserActiveDegreeController {

	@Autowired 
	private UserActiveDegreeService userActiveDegreeService;
	
	@PostMapping("/calculate")
	public ReturnDataUtil calculateUserActive(@RequestBody(required = false) UserActiveDegreeForm form,
			HttpServletRequest request){
		if(form == null){
			form = new UserActiveDegreeForm();
		}
		if(form.getCompanyId() == null){
			Integer companyId = (Integer)request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID);
			form.setCompanyId(companyId);
		}
		TotalResultBean<List<UserActiveDegreeBean>> activeTotalResult = userActiveDegreeService.
				calculateUserActiveGegree(form);
		UserActiveDegreeBean statistics = statistics(activeTotalResult.getResult());
		activeTotalResult.getResult().add(statistics);
		return ResultUtil.success(activeTotalResult.getResult(),form.getCurrentPage(),activeTotalResult.getTotal());
	}
	
	
	/**
	 * 导出用户活跃度
	 * @author hebiting
	 * @date 2018年12月11日下午6:18:47
	 * @param form
	 * @param request
	 * @param response
	 */
	@PostMapping("/export")
	public void export(@RequestBody(required = false) UserActiveDegreeForm form,
			HttpServletRequest request,HttpServletResponse response){
		if(form == null){
			form = new UserActiveDegreeForm();
		}
		if(form.getCompanyId() == null){
			Integer companyId = (Integer)request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID);
			form.setCompanyId(companyId);
		}
		TotalResultBean<List<UserActiveDegreeBean>> activeTotalResult = userActiveDegreeService.
				calculateUserActiveGegree(form);
		UserActiveDegreeBean statistics = statistics(activeTotalResult.getResult());
		activeTotalResult.getResult().add(statistics);
		String title = "用户活跃度统计";
		StringBuffer date = new StringBuffer("起始--至今");
		if (form.getStartTime() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDDHHMMSS(form.getStartTime()));
		}
		if (form.getEndTime() != null) {
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDDHHMMSS(form.getEndTime()));
		}
		if(form.getIsShowAll()==0){
			date.append("--第"+form.getCurrentPage()+"页");
		}
		//设置导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(form.getStartTime()!=null&&form.getEndTime()!=null) {
			//导出日志内容按时间格式导出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(form.getStartTime())+"--"+DateUtil.formatYYYYMMDD(form.getEndTime())+"的销售记录的数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出销售记录--当前页的数据");
		}
		String[] headers = new String[] { "date","registerNum","noPwpayNum","cancelNopwpayNum","attentionNum",
				"cancelAttentionNum","receivePercentNum", "openMachinesNum","createOrderNum",
				"usedPercentNum","allBuyNum"};
		String[] column = new String[] { "统计日期","注册数量", "签约免密数量","取消免密数量","关注数量","取消关注数量",
				"领取优惠券数量","开门数量", "订单数量", "使用优惠券数量", "总购买商品数量" };
		try {
			ExcelUtil.exportExcel(title.toString(), headers, column, response, activeTotalResult.getResult(), date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 合计活跃度数据
	 * @author hebiting
	 * @date 2018年12月11日下午6:18:59
	 * @param userActiveList
	 * @return
	 */
	private UserActiveDegreeBean statistics(List<UserActiveDegreeBean> userActiveList){
		UserActiveDegreeBean statistics = new UserActiveDegreeBean();
		statistics.setDate("合计");
		for(UserActiveDegreeBean userAc : userActiveList){
			statistics.setAllBuyNum(statistics.getAllBuyNum()+userAc.getAllBuyNum());
			statistics.setAttentionNum(statistics.getAttentionNum()+userAc.getAttentionNum());
			statistics.setCancelAttentionNum(statistics.getCancelAttentionNum()+userAc.getCancelAttentionNum());
			statistics.setCancelNopwpayNum(statistics.getCancelNopwpayNum() + userAc.getCancelNopwpayNum());
			statistics.setCreateOrderNum(statistics.getCreateOrderNum()+userAc.getCreateOrderNum());
			statistics.setNoPwpayNum(statistics.getNoPwpayNum()+userAc.getNoPwpayNum());
			statistics.setOpenMachinesNum(statistics.getOpenMachinesNum() + userAc.getOpenMachinesNum());
			statistics.setReceivePercentNum(statistics.getReceivePercentNum() + userAc.getReceivePercentNum());
			statistics.setWxRegisterNum(statistics.getWxRegisterNum()+userAc.getWxRegisterNum());
			statistics.setAliRegisterNum(statistics.getAliRegisterNum()+userAc.getAliRegisterNum());
			statistics.setRegisterNum(statistics.getRegisterNum() + userAc.getRegisterNum());
			statistics.setUsedPercentNum(statistics.getUsedPercentNum() +userAc.getUsedPercentNum() );
		}
		return statistics;
	}
}
