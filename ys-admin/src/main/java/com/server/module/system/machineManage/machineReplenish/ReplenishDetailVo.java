package com.server.module.system.machineManage.machineReplenish;

import lombok.Data;

@Data
public class ReplenishDetailVo {

   private  int wayNumber;
   private String code;
   private String itemName;
   private int num;
   private int fullNum;
   private Long itemId;

   private String address;
}
