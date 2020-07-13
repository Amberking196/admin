package com.server.module.customer.coupon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;

import java.util.Iterator;
import java.util.List;

/**
 * author name: yjr
 * create time: 2018-07-06 14:05:39
 */
@Api(value = "CouponFrontController", description = "优惠劵")
@RestController
@RequestMapping("/frontCoupon")
public class CouponFrontController {


    @Autowired
    private CouponService couponServiceImpl;

    @ApiOperation(value = "优惠劵列表", notes = "myList", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/myList", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil myList(CouponCondition condition) {
        condition.setPageSize(1000);
        return couponServiceImpl.listPage(condition);
    }

    @ApiOperation(value = "优惠劵领取", notes = "addCouponToCustomer", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/addCouponToCustomer", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil addCouponToCustomer(Integer couponId) {
        return couponServiceImpl.addCouponToCustomer(couponId);
    }

    @ApiOperation(value = "可用优惠劵列表", notes = "usableCoupons", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/usableCoupons", produces = "application/json;charset=UTF-8")
    public List<CouponVo> usableCoupons(Long[] productIds, Double[] prices ) {
        return  couponServiceImpl.usableCoupons(productIds,prices);
    }

    @ApiOperation(value = "查询优惠券下绑定的商品", notes = "findShoppingGoodsBean", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/findShoppingGoodsBean", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil findShoppingGoodsBean(Integer couponId) {
    	List<ShoppingGoodsBean> list = couponServiceImpl.findShoppingGoodsBean(couponId);
    	return new ReturnDataUtil(list);
        
    }

}

