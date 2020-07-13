package com.server.module.system.companyManage.companyMachines;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  replenish_company_machines
 * author name: yjr
 * create time: 2018-09-14 15:10:24
 */ 
@Data
public class ReplenishCompanyMachinesCondition extends PageAssist{

        Long companyId;
        Long areaId;
        Long lineId;
        
        Long otherCompanyId;// 第三方公司id
        
}

