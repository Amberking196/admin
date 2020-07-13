package com.server.module.customer.questionnaire;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Answer {

    private Integer id;
    private Integer userId;
    private Integer testId;
    private Date createTime;
    private String suggest;
    List<AnswerItem> itemList= Lists.newArrayList();
}
