package com.server.module.system.shoppingManager.customerOrder;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.module.commonBean.PageAssist;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 用户消费记录条件
 */
@Data
public class UsersConsumptionRecordForm extends PageAssist {

    // 手机号
    private Long phone;

    // 公司Id
    private String companyId;

    // 子公司Id
    private String companyIds;

    // 消费金额
    private BigDecimal price;

    // 查询的金额条件是大于还是小于
    // 1 表示小于，2表示大于
    private Integer state;

    private List<String> phoneList;
    private String content;
    
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    
    private String vmCode;
}
