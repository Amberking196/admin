package com.server.module.system.logsManager.exportLog;

import com.server.common.persistence.Entity;
import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: export_log author name: yjr create time: 2018-09-14 10:31:38
 */
@Data
@Entity(tableName = "export_log", id = "id", idGenerate = "auto")
public class ExportLogBean {

	private Long id;
	private Long operator;
	private String operatorName;
	private Long companyId;
	private String content;
	private Date createTime;
	private String companyName;//公司名称

}
