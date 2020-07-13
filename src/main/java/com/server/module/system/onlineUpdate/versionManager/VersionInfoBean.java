package com.server.module.system.onlineUpdate.versionManager;

import java.util.Date;

import lombok.Data;
@Data
public class VersionInfoBean {

	private Integer id;
	//版本信息
	private String versionInfo;
	private Integer pid;
	private Date createTime;
	private Date updateTime;
	private Long createUser;
	private Long updateUser;
	private String remark;
	private Integer deleteFlag;
	
	//非字段
	private String pversion;
	private String createUserName;
	private String updateUserName;
}
