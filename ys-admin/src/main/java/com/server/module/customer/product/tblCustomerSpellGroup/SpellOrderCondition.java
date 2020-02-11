package com.server.module.customer.product.tblCustomerSpellGroup;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

@Data
public class SpellOrderCondition extends PageAssist {

    private String start;//开始时间
    private String end;//结束时间
    private Integer state;//状态
    private String goodsName;//商品名称
    private String ptCode;//订单号
    private String phone;//参团人手机
}
