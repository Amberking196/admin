package com.server.module.system.couponManager.couponCustomer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerVo {

//c.phone,c.vmCode,c.createTime ,m.couponId,cu.id

    private  String phone;
   // private String vmCode;
    private Long couponId;
    private Long customerId;
    private Long couponCustomerId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String addLabel;


}
