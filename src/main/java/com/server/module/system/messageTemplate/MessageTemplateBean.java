package com.server.module.system.messageTemplate;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: message_template author name: why create time: 2019-01-07
 * 11:21:04
 */
@Data
@Entity(tableName = "message_template", id = "id", idGenerate = "auto")
public class MessageTemplateBean {

	//主键id
	private Long id;
	@NotField
	private Integer num;
	//短信主题
	private String theme;
	//短信内容
	private String content;
	//状态 0未审核  1 已通过
	private Integer state;
	//创建时间
	private Date createTime;
	//创建用户
	private Long createUser;
	//更新时间
	private Date updateTime;
	//更新用户
	private Long updateUser;
	//是否删除 0未删除  1 已删除
	private Integer deleteFlag;
	//公司id
	private Long companyId;
	
	@NotField
	private String stateLabel;
	@NotField
	private String companyName;
	
	public String getStateLabel() {
		if(state==0) {
			stateLabel="未审核";
		}
		if(state==1) {
			stateLabel="已通过";
		}
		return stateLabel;
	}

}
