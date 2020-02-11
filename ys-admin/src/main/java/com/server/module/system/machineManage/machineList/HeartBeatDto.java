package com.server.module.system.machineManage.machineList;

import java.util.Date;

import lombok.Data;

@Data
public class HeartBeatDto {
	private String factoryNumber;
	private String vmCode;
	private String beforeHeartBeat;
	private String afterHeartBeat;
	private String remark;

}
