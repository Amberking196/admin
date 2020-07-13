package com.server.module.system.machineManage.machinesWayItem;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * table name:  vending_machines_way_item
 * author name: yjr
 * create time: 2018-08-31 14:03:10
 */ 
@Data
public class VendingMachinesWayItemCondition extends PageAssist{

  private Long wayId;
  
  //下面属性为补货报表用
  private Long companyId;
  private Integer areaId;
  private Long lineId;
  private Integer rate;// 10  20  ... 100,  缺货率  
  private String startTime;
  private String endTime;
  
  private Integer type;// 1 版本一  2  版本2
  private String vmCode;
}

