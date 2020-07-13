package com.server.module.system.machineManage.machineReplenish;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MachineReplenishDto {
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date createTime; //补货时间
	private String name;//商品名称
	private Integer num;//补货数量
	private String operateName;// 补货操作人的名字

	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
	
}
