package com.server.module.system.machineManage.machinesReplenishContrast;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

/**
 * 
 * @author why
 * @date: 2019年5月14日 下午5:46:44
 */
@Data
@Entity(tableName = "machines_replenish_contrast", id = "id", idGenerate = "auto")
public class MachinesReplenishContrastBean {

	// 主键id
	private Long id;
	// 机器编码
	private String vmCode;
	// 货道号
	private Integer wayNumber;
	// 上次补完后数量
	private Integer lastReplenishNum;
	// 本次输入数量
	private Integer thisReplenishNum;
	// 销售数量
	private Integer salesQuantity;
	// 当前库存
	private Integer thisRepertory;
	//开始时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	// 创建时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	// 创建用户
	private Long createUser;
	// 更新时间
	private Date updateTime;
	// 更新用户
	private Long updateUser;
	// 是否删除
	private Integer deleteFlag;
	//补货时机器库存
	private Integer thisReplenishRepertory;
	
	private Integer state;
	
	@NotField
	private String stateName;
	//补货人名称
	@NotField
	private String name;
	//公司名称
	@NotField
	private String companyName;
	//机器地址
	@NotField
	private String locatoinName;

}
