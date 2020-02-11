package com.server.module.system.carryWaterVouchersManage.carryWaterVouchersProduct;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: carry_water_vouchers_product author name: why create time:
 * 2018-11-03 16:25:36
 */
@Data
public class CarryWaterVouchersProductForm extends PageAssist {

	//提水券id
    private int carryId;
    //是否绑定   0未绑定   1已绑定
    private int isBind;
    //商品名称 
    private String name;
}
