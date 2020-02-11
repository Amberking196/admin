package com.server.module.system.replenishManage.replenishCorrect;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class ReplenishForm {

	private String vmCode;
	private Integer wayNumber;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	
	private Integer companyId;
}
