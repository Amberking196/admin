package com.server.module.system.machineManage.machinesWayTemplate;


import lombok.Data;

import java.util.Date;

@Data
public class WayItemDto {

    // 模板详情Id
    private Long templateDetailId;

    // 模板Id
    private Long templateId;

    // 货道号
    private Integer wayNumber;

    // 商品Id
    private Integer itemId;

    // 商品名称
    private String itemName;

    // 货道最大容量
    private Integer maxCapacity;

    // 货道当前容量
    private Integer curCapacity;

    // 商品成本价
    private Double costPrice;

    // 商品销售价
    private Double price;

    // 商品是否热卖
    private Integer hot;

    // 商品的图片名称
    private String pic;

    // 过期时间---2018年8月3日17:18:15不知道有什么用
    private Date endTime;

    // vending_machines_item 的id
    private Long machineItemId;

/*
    -- machine way的 itemId存储的是vending_machines_item 的id
    -- vending_machines_item 的basicItemId 存储的是item_basic的id
*/

}
