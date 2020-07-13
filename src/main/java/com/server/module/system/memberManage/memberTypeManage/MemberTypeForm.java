package com.server.module.system.memberManage.memberTypeManage;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  member_type
 * author name: why
 * create time: 2018-09-20 16:08:54
 */ 
@Data
public class MemberTypeForm extends PageAssist{

	//等级id 
	Integer typeId;
	
	//判断是会员类型下拉列表  0    还是会员管理下拉列表   1
	Integer flag;
}

