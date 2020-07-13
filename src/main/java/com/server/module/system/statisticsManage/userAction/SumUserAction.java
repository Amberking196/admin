package com.server.module.system.statisticsManage.userAction;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class SumUserAction {
	private BigDecimal sumFinishedMoney=new BigDecimal(0);
	private BigDecimal sumMachinesNum=new BigDecimal(0);
	private BigDecimal sumFinishedItemNum=new BigDecimal(0);
	private BigDecimal sumFinishedOrderNum=new BigDecimal(0);
	private BigDecimal sumAverageMoney=new BigDecimal(0);
	private BigDecimal sumNormalNum=new BigDecimal(0);
	private BigDecimal sumAverage=new BigDecimal(0);
	private BigDecimal averageMaxPirce=new BigDecimal(0);
	private BigDecimal averageMinPirce=new BigDecimal(10);
}
