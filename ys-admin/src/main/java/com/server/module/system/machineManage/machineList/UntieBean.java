package com.server.module.system.machineManage.machineList;


import lombok.Data;

@Data
public class UntieBean {


    // 售货机编号
    private String vmCode;

    // 备注信息
    private String remark;

    // 出厂编号
    private String factoryNumber;

    // 用户的Id
    private Long userId;


}
