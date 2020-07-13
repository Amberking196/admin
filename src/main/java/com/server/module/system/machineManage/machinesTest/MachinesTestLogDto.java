package com.server.module.system.machineManage.machinesTest;

import lombok.Data;

@Data
public class MachinesTestLogDto {
	private Long id;
	private String vmCode;
	private String stateChange;
	private String startTime;
	private String endTime;
	private String userName;
	private String remark;
	private String successOrFail;
	private String testName;
}
