package com.server.module.system.statisticsManage.customerGroup.customerEvent;

import lombok.Data;

@Data
public class CustomerEventStateVo {
	String fromTo;
	String name;
	public CustomerEventStateVo(String fromTo, String name) {
		super();
		this.fromTo = fromTo;
		this.name = name;
	}
	 
}
