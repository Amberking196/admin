package com.server.module.system.userManage;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class CustomerDto {

	String vendingMachinesCode;
	String itemName;
	Integer quantity;
}
