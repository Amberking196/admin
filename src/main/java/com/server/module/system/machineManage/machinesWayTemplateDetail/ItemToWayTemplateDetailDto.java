package com.server.module.system.machineManage.machinesWayTemplateDetail;


import lombok.Data;


/**
 * 商品绑定到货道模板详情实体类
 */
@Data
public class ItemToWayTemplateDetailDto {

    // 模板详情Id
    private Long id;

    // 商品Id
    private Long basicItemId;

    // 商品销售价格
    private Double price;

    // 成本价格
    private Double costPrice;

    // 货道号
    private Integer wayNumber;

    // 最大容量
    private Integer fullNum;

    // 当前容量
    private Integer num;

    // 是否热卖
    private Integer hot;

    //公司Id
    private Long companyId;

    // 模板Id
    private Long templateId;




}
