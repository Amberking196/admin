package com.server.module.system.machineManage.machinesPic;

import java.util.List;
import java.util.Set;
import lombok.Data;

/**
 * table name: vending_machines_advertising author name: why create time:
 * 2018-11-02 10:38:21
 */
@Data
public class VendingMachinesPicDto {
	private Set<Integer> companyIds;
	private Set<String> vmCodes;
	private Set<Integer> vmiCompanyIds;
	private List<String> h;
	
}
