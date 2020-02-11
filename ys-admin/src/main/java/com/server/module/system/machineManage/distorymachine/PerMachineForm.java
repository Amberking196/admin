package com.server.module.system.machineManage.distorymachine;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

@Data
public class PerMachineForm extends PageAssist {

    private Integer period=0;// 1 一小时内  2 一天内  3 一周内

    private String companyId;

    private String vmCode;

    private String companyIds;
    
    Integer areaId;

}
