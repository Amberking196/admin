package com.server.module.system.couponManager.couponProduct;

import lombok.Data;

@Data
public class CouponProductForm {

    private int couponId;
    private int useWhere;
    private int isBind;
    private String name;
}
