package com.server.module.customer.questionnaire;

import com.server.common.persistence.NotField;
import lombok.Data;

@Data
public class Option {
    private Integer id;
    private String title;
    private String content;
    private Integer topicId;
    @NotField
    private Integer answerCount;//选该答案的数量
}
