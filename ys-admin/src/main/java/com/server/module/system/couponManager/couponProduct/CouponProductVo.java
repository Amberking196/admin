package com.server.module.system.couponManager.couponProduct;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class CouponProductVo {
    private Integer id;
    private Integer couponId;
    private Long productId;
    private String productName;

    private String bindLabel;
}
