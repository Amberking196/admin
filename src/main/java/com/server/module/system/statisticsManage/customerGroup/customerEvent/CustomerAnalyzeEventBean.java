package com.server.module.system.statisticsManage.customerGroup.customerEvent;

import com.server.common.persistence.Entity;
import lombok.Data;

import java.util.Date;


/**
 * table name: customer_analyze_event author name: yjr create time: 2018-10-18
 * 09:10:39
 */
@Data
@Entity(tableName = "customer_analyze_event", id = "id", idGenerate = "auto")
public class CustomerAnalyzeEventBean {

	// @JsonIgnore public String tableName="customer_analyze_event";
	// @JsonIgnore public String selectSql="select * from customer_analyze_event
	// where 1=1 ";
	// @JsonIgnore public String selectSql1="select
	// id,customerId,fromState,currState,fireTime,createTime,eventType from
	// customer_analyze_event where 1=1 ";
	private Long id;
	private Long customerId;
	private Integer fromState;
	private Integer currState;
	private Date fireTime;
	private Date createTime;
	private Integer eventType;

}
