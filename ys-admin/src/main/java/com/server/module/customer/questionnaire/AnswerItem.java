package com.server.module.customer.questionnaire;

import lombok.Data;

@Data
public class AnswerItem {
    private Integer id;
    private Integer answerId;
    private Integer topicId;
    private Integer optionId;
}
