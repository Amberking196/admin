package com.server.module.customer.questionnaire.topic;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: questionnaire_topic author name: yjr create time: 2019-01-25
 * 16:01:45
 */
@Data
public class QuestionnaireTopicCondition extends PageAssist {

	private Long testId;
	private Long companyId;
}
