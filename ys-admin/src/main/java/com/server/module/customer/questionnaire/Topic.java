package com.server.module.customer.questionnaire;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class Topic {

    private Integer id;
    private String question;
    private Integer testId;
    private Integer order;
    private Integer oneAnswer;
    private List<Option> list= Lists.newArrayList();
}
