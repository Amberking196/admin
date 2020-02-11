package com.server.module.customer.complain.replyCommonLanguage;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: complain_reply_common_language author name: why create time:
 * 2019-01-04 09:50:27
 */
@Data
@Entity(tableName = "complain_reply_common_language", id = "id", idGenerate = "auto")
public class ComplainReplyCommonLanguageBean {

	//主键id
	private Long id;
	@NotField
	private Integer num;
	//常用语
	private String commonLanguage;
	//创建时间
	private Date createTime;
	//创建用户
	private Long createUser;
	//更新时间
	private Date updateTime;
	//更新用户
	private Long updateUser;
	//是否删除 0 未删除 1已删除
	private Integer deleteFlag;

}
