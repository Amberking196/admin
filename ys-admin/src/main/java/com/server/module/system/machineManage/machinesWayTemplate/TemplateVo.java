package com.server.module.system.machineManage.machinesWayTemplate;


import lombok.Data;

import java.util.Date;

@Data
public class TemplateVo {


    private Long id;
    private String templateName;
    private String companyName;
    private String areaName;
    private String creatorName;

    private Date createTime;
    private Date updateTime;

}
