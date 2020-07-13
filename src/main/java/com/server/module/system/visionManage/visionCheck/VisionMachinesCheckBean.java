package com.server.module.system.visionManage.visionCheck;

import com.server.common.persistence.Entity;
import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  vision_machines_check
 * author name: hjc
 * create time: 2019-10-10 10:54:34
 */ 
@Data
@Entity(tableName="vision_machines_check",id="id",idGenerate="auto")
public class VisionMachinesCheckBean{
	private Long id;
	private String vmCode;
	private Date createTime;
	private String checkId;
	private String remark;
	private String version;
}

