package com.server.module.system.couponManager.couponCustomer;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

@Data
public class ConponCustomerNoteForm extends PageAssist {

	//售货机编号
	private String vmCode;
	//有效天数
	private Integer days;
	//类型   1.优惠券 2.提水券
	private  Integer type;
	// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date startTime;
	// 结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date endTime;
	//公司id
	private Long companyId;
	//手机号
	private String phone;
	//状态  0 未发送  1 已发送
	private Integer state;
	
	//短信内容
	private String content;
	//用户手机号
	private List<String> phoneList;
	
	private String id;
	
}
