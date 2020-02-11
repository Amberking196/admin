package com.server.module.system.machineManage.machinesWayTemplateDetail;

import com.server.common.persistence.Entity;
import lombok.Data;

/**
 * table name:  vending_machines_way_template_del
 * author name: zfc
 * create time: 2018-08-03 10:35:22
 */
@Data
@Entity(tableName = "vending_machines_way_template_del", id = "id", idGenerate = "auto")
public class VendingMachinesWayTemplateDelBean {


/*    @JsonIgnore
    public String tableName = "vending_machines_way_template_del";
    @JsonIgnore
    public String selectSql = "select * from vending_machines_way_template_del where 1=1 ";
    @JsonIgnore
    public String selectSql1 = "select id,templateId,wayNumber,itemId,price,maxCapacity,curCapacity from vending_machines_way_template_del where 1=1 ";

    */

    // 模板详情Id
    private Long id;

    // 模板主表Id
    private Long templateId;

    // 货道号
    private Integer wayNumber;

    // 商品号
    private Long itemId;

    // 价钱
    private Double price;

    // 货道最大容量
    private Long maxCapacity;

    // 货道当前容量，默认为0
    private Long curCapacity;

    // 成本价
    private Double costPrice;

}

