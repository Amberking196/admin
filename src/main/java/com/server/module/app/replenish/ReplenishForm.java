package com.server.module.app.replenish;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ReplenishForm {

	private Integer companyId;
	private String companyIds;
	private String vmCode;
	private Long dutyId;
	private Integer areaId;
	private Integer lineId;
	private BigDecimal rate;
	private String address;
	int version;//  1  2
	private Integer otherCompanyId;

	private Integer level;
	private Integer type;//补货公司为1 



}
