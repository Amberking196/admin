package com.server.module.system.officialManage.officialMessage;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name:  official_message
 * author name: hjc
 * create time: 2018-08-02 10:24:08
 */ 
@Data
public class OfficialMessageForm extends PageAssist{
	private String phone;//电话
	private String name;//姓名
	private String mail;//邮箱

}

