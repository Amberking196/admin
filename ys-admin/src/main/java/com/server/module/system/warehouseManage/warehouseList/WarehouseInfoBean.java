package com.server.module.system.warehouseManage.warehouseList;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  warehouse_info
 * author name: why
 * create time: 2018-05-14 22:06:48
 */ 
@Data
@Entity(tableName="warehouse_info",id="id",idGenerate="auto")
public class WarehouseInfoBean{

	
	//仓库编号
	private Long id;
	//所属公司Id
	private Long companyId;
	//负责人id
	private Integer principal;
	//仓库名称
	private String name;
	//仓库地址
	private String location;
	//联系方式
	private String phone;
	//状态 （60001 启用  60002 禁用）
	private Integer state;
	//创建时间
	private Date createTime;
	private String time;
	//是否删除 '0' 未删除 '1' 已删除
	private Integer delFlag;
	
	//公司名称
	@NotField
	private String companyName;
	//负责人名称
	@NotField
	private String principalName;
	//状态名称
	@NotField
	private String stateName;
	
	//给前端做判断
	@NotField
	private Integer isShow;

}

