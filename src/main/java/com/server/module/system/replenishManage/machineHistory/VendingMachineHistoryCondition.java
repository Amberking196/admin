package com.server.module.system.replenishManage.machineHistory;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * table name:  vending_machine_history
 * author name: yjr
 * create time: 2018-11-06 14:32:26
 */ 
@Data
public class VendingMachineHistoryCondition extends PageAssist{

     String vmCode;
     String companyId;
     String startDay; //2018-09-09
     String endDay; //2018-09-09
     int balance;//按照balance查询  0 不处理  1 不平衡
     boolean replenish;
     boolean sale;
     Integer type;//1 机器 0 商品  分类方式
     Integer areaId;
}

