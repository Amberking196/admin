package com.server.module.system.memberManage.memberManager;

import java.util.List;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

@Data
public class MemberForm extends PageAssist{

	//用户电话
	private String phone;
	//用户状态
	private Integer state;
	//是否发送
	private Integer isSend;
	//是否关注
	private Integer follow;
	//短信内容
	private String content;
	//用户手机号
	private List<String> phoneList;
	
	private String id;
	private Integer companyId;
	private String companyIds;
}
