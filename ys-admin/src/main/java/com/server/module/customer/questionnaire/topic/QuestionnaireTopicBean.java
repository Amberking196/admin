package com.server.module.customer.questionnaire.topic;

import com.google.common.collect.Lists;
import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import com.server.module.customer.questionnaire.option.QuestionnaireOptionBean;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * table name: questionnaire_topic author name: yjr create time: 2019-01-25
 * 16:01:45
 */
@Data
@Entity(tableName = "questionnaire_topic", id = "id", idGenerate = "auto")
public class QuestionnaireTopicBean {

	private Long id;
	private String question;
	// private Integer order;
	private Integer oneAnswer;
	private Long companyId;
	@NotField
	private List<QuestionnaireOptionBean> optionBeanList = Lists.newArrayList();

}
