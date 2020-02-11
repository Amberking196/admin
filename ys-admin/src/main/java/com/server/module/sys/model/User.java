package com.server.module.sys.model;

import lombok.Data;

@Data
public class User {
	
	private Long id;
	private String name;
	private Integer companyId;
	private String openId;
	private Integer type;
	private Integer payType;

}
