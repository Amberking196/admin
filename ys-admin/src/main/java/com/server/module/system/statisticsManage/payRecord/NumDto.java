package com.server.module.system.statisticsManage.payRecord;

import lombok.Data;

@Data
public class NumDto {

	private Long basicItemId;
	private String itemName;
	private Integer unitWeight;
	private Integer preNum;
	private Integer posNum;
	private Integer buyNum;
}
