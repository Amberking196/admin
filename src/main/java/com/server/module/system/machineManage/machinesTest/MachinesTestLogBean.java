package com.server.module.system.machineManage.machinesTest;

import java.util.Date;

import lombok.Data;
@Data
public class MachinesTestLogBean {

	private Long id;
	private String vmCode;
	private Integer preState;
	private Integer currState;
	private String remark;
	private Date startTime;
	private Date endTime;
	private Long createUser;
	private Integer success;
}
