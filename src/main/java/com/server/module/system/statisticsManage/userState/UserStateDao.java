package com.server.module.system.statisticsManage.userState;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayForm;

public interface UserStateDao {
	//用户转换状态数量统计
	public UserStateBean stateCompare(PayRecordPerDayForm payDayForm);
	
	//注册人数 
    public Integer userRegisterNum(PayRecordPerDayForm payDayForm);
	
	//注册人数 
    public Integer customerRegisterNum(PayRecordPerDayForm payDayForm);
	//注册人数 
    public List<CustomerRegisterNumDto> customerRegisterNumGroupByDay(PayRecordPerDayForm payDayForm);
	//注册人数  昨天 上周 上月
    public List<CustomerRegisterNumDto> customerRegisterNumGroupByDay(PayRecordPerDayForm payDayForm,PayRecordPerDayForm payDayForm1,PayRecordPerDayForm payDayForm2);
    
    
	//用户各状态总数量
	public UserStateBean userStateNum(PayRecordPerDayForm payDayForm);
	
	//用户当前状态总数量
	public UserStateBean userCurrStateNum(PayRecordPerDayForm payDayForm);

	//用户当前状态总数量
	public UserStateBean userFromStateNum(PayRecordPerDayForm payDayForm);
	
	//用户状态变化总数量
	public UserStateBean userChangeNum(PayRecordPerDayForm payDayForm);
	
	//用户各状态总数量
	public LinkedHashMap userStateGroupByDate(PayRecordPerDayForm payDayForm);
	

	
}
