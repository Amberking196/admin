package com.server.module.customer.questionnaire.topic;

import lombok.Data;

@Data
public class TopicVO {
    private Long id;
    private String question;
    //private Integer order;
    private Integer oneAnswer;
    private Long topicId;
}
