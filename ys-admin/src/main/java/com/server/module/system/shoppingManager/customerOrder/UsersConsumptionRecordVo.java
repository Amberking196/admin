package com.server.module.system.shoppingManager.customerOrder;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户消费记录
 */
@Data
public class UsersConsumptionRecordVo {

    // 用户Id
    private Long customerId;

    // 用户昵称
    private String nickName;

    // 用户手机号
    private Long phone;

    // 消费金额
    private BigDecimal price ;

    // 消费次数
    private BigDecimal count;

    // 开始时间和结束时间
    private String startTime;

    private String endTime;
    
    //注册机器
    private String vmCode;

}
