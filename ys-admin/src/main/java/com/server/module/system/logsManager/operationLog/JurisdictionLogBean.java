package com.server.module.system.logsManager.operationLog;

import java.util.Date;
import com.server.common.persistence.Entity;
import lombok.Data;

/**
 * table name: jurisdiction_log author name: why create time: 2018-05-07
 * 13:49:31
 */
@Data
@Entity(tableName = "jurisdiction_log", id = "id", idGenerate = "auto")
public class JurisdictionLogBean  {

	// 序号
	private Long id;
	// 公司Id
	private Integer companyId;
	// 用户名
	private String userName;
	// 售货机编号
	private String vmCode;
	// 内容
	private String content;
	// 操作时间
	private String operationTime;

}
