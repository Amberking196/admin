package com.server.module.system.machineManage.machinesWayItem;

import lombok.Data;

@Data
public class ReplenishItemVo {
	 private String vmCode;
	
	 private Integer wayNumber;
	 private int percent;
	 
	 private Integer num;
	 private Integer fullNum;
	 private Integer replenishNum;
	 private Integer outQuantity;
	 
	 
	 private String name;
	 private String simpleName;
	 
	 public String getSimpleName(){
		 if(simpleName!=null)
			 return simpleName;
		 
		 return name;
	 }
	 
	
}
