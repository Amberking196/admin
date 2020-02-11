package com.server.module.system.couponManager.couponProduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * author name: yjr
 * create time: 2018-07-10 10:28:12
 */
@Api(value = "CouponProductController", description = "优惠劵商品绑定")
@RestController
@RequestMapping("/couponProduct")
public class CouponProductController {


    @Autowired
    private CouponProductService couponProductServiceImpl;
/*
    @ApiOperation(value = "优惠劵商品绑定列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(CouponProductCondition condition) {
        return couponProductServiceImpl.listPage(condition);
    }*/

    @ApiOperation(value = "优惠劵商品绑定列表", notes = "list", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public List<CouponProductVo> list(CouponProductForm condition) {
        // 0  未绑定  1 已绑定

        return couponProductServiceImpl.list(condition);
    }
    @ApiOperation(value = "优惠劵商品绑定", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(@RequestBody CouponProductBean entity) {
        return new ReturnDataUtil(couponProductServiceImpl.add(entity));
    }
    @ApiOperation(value = "优惠劵商品绑定多个", notes = "addAll", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/addAll", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil addAll(Long[] productIds,Integer couponId) {

        for (int i = 0; i < productIds.length; i++) {

            CouponProductBean bean=new CouponProductBean();
            bean.setCouponId(couponId*1l);
            bean.setCreateUser(1l);
            bean.setProductId(productIds[i]);
            couponProductServiceImpl.add(bean);

        }
        return new ReturnDataUtil();
    }
/*
    @ApiOperation(value = "优惠劵商品绑定修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody CouponProduct1Bean entity) {
        return new ReturnDataUtil(couponProductServiceImpl.update(entity));
    }*/

    @ApiOperation(value = "优惠劵商品绑定删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil del(Object id) {
        return new ReturnDataUtil(couponProductServiceImpl.del(id));
    }
}

