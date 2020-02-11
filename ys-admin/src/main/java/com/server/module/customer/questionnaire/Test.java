package com.server.module.customer.questionnaire;

import com.google.common.collect.Lists;
import com.server.common.persistence.NotField;
import lombok.Data;

import java.util.List;

@Data
public class Test {
    private Integer id;
    private String title;
    private String remark;
    private Integer state;
    private Integer type;//使用人群
    private List<Topic> list= Lists.newArrayList();
    @NotField
    private Integer answerAccount;//答题人次

    private String smsContent;//告知短信内容
}
