package com.server.module.system.itemManage.itemBasic;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
@Data
public class MyItemCondition extends PageAssist {
   private Integer type;//类型   1 我的库  2  基础库  3 全部
   private String name;//商品名
   private String vmCode;
}
