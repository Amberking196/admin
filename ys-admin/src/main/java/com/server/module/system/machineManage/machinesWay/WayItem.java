package com.server.module.system.machineManage.machinesWay;

import com.server.common.persistence.NotField;

import lombok.Data;

@Data
public class WayItem {

   // private Long machineWayId;
    //private Long wayNumber;
	private Long id;
    private Long basicItemId;
    private String itemName;
    private String itemPic;
    private Double price;
    private Double promotionPrice;
    private Long num;
    private Long fullNum;
    private int weight;
    private int orderNumber;
    private Long picId;
    private String topLeftPic;
    //每个商品的销量
  	@NotField
  	private Integer salesVolume;
  	//货道推荐容量
  	@NotField
  	private Integer recommendCapacity;
}
