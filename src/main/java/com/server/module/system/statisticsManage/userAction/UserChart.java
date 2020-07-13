package com.server.module.system.statisticsManage.userAction;

import java.util.List;

import lombok.Data;
@Data
public class UserChart {

	private String name;
	private String unit;
	private List<Double> y;
	
	public UserChart(String name, String unit, List<Double> y) {
		this.name=name;
		this.unit=unit;
		this.y=y;
	}

	public UserChart() {
		// TODO Auto-generated constructor stub
	}
}
