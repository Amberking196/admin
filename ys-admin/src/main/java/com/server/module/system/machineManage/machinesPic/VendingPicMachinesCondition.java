package com.server.module.system.machineManage.machinesPic;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * table name:  vending_advertising_machines
 * author name: yjr
 * create time: 2018-12-24 15:45:31
 */ 
@Data
public class VendingPicMachinesCondition extends PageAssist{

   private Long picId;//必须传
   private Integer companyId;//必须传
   private Integer areaId;
   private String vmCode;

   private int range;//0 全部  1 已绑定 2 未绑定
}

