package com.server.module.system.couponManager.couponMachine;

import lombok.Data;

@Data
public class CouponMachineInfoVo {

    private  String code;
    private String locatoinName;
    private Long companyId;
    private Long areaId;
    private Integer couponMachineId;

    private String companyName;
    private String areaName;
    private String addLabel;
}
