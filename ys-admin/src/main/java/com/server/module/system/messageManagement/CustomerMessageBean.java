package com.server.module.system.messageManagement;

import com.server.common.persistence.Entity;
import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: customer_message author name: why create time: 2018-08-17
 * 08:48:16
 */
@Data
@Entity(tableName = "customer_message", id = "id", idGenerate = "auto")
public class CustomerMessageBean {

	//主键ID
	private Long id;
	//用户ID
	private Long customerId;
	//用户手机号
	private String phone;
	//留言内容
	private String content;
	//图片名称
	private String picName;
	//视频名称
	private String video;
	//创建时间
	private Date createTime;
	//创建用户
	private Long createUser;
	//更新时间
	private Date updateTime;
	//更新用户
	private Long updateUser;
	//是否删除 0 未删除  1 已删除
	private Integer deleteFlag;
	//售货机编号
	private String vmCode;
	//留言类型
	private String type;

}
