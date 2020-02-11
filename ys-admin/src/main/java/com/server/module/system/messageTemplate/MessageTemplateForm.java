package com.server.module.system.messageTemplate;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: message_template author name: why create time: 2019-01-07
 * 11:21:04
 */
@Data
public class MessageTemplateForm extends PageAssist {

		//短信主题
		private String theme;
		//状态 0未审核  1 已通过
		private Integer state;
}
