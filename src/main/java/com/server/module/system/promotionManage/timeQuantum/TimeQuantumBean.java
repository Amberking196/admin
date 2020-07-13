package com.server.module.system.promotionManage.timeQuantum;

import java.util.Date;
import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import lombok.Data;

/**
 * table name: time_quantum author name: why create time: 2018-08-22 10:56:46
 */
@Data
@Entity(tableName = "time_quantum", id = "id", idGenerate = "auto")
public class TimeQuantumBean {

	//主键id
	private Long id;
	//公司iD
	private Long companyId;
	//时间段
	private String timeSlot;
	//折扣
	private Double rebate;
	//创建时间
	private Date createTime;
	//创建用户
	private Long createUser;
	//创建时间
	private Date updateTime;
	//更新用户
	private Long updateUser;
	//是否删除 0 未删除  1 已删除
	private Integer deleteFlag;
	
	//公司名称
	@NotField
	private String companyName;
	//序号
	@NotField
	private Integer number;

}
