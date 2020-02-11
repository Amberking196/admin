package com.server.module.system.statisticsManage.userState;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.NotField;

import lombok.Data;

@Data
public class UserStateBean {
	private double registerNum;//新用户数
	private double sumNum;//总人数
	private double one;//一次
	private double two;
	private double three;
	private double four;
	private double five;
	private double six;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	private String name;
	@NotField
	private double sort;
}
