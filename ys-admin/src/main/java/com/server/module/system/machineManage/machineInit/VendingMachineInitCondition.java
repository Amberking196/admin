package com.server.module.system.machineManage.machineInit;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table  name: vending_machine_init
 * author name: why 
 * create time: 2018-04-03 09:39:55
 */
@Data
public class VendingMachineInitCondition extends PageAssist {

	String vendingMachinesCode;
	String state;
	@DateTimeFormat(iso=ISO.DATE) Date startDate;
	@DateTimeFormat(iso=ISO.DATE) Date endDate;
}
