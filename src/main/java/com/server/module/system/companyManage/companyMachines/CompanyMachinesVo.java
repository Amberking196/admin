package com.server.module.system.companyManage.companyMachines;

import java.util.Date;

import lombok.Data;

@Data
public class CompanyMachinesVo {
	//vmCode, i.locatoinName,r.id,r.companyId,r.code,
	
	
    String vmCode;
    String locatoinName;
    Long id;
    Long companyId;
    String code;
    Date createTime;
	
	
	
}
