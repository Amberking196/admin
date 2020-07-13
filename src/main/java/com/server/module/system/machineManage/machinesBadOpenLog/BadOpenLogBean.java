package com.server.module.system.machineManage.machinesBadOpenLog;

import java.util.Date;

import lombok.Data;
@Data
public class BadOpenLogBean {

	private String vmCode;
	private Long id;
	private Long customerId;
	private Date createTime;
}
