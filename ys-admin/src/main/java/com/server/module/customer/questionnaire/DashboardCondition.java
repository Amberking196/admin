package com.server.module.customer.questionnaire;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

@Data
public class DashboardCondition  extends PageAssist {

    private String vmCode;
    private String phone;
    private String type;// 用户群类

    private int sendFlag;//0 全部  1 已发 2 未发

}
