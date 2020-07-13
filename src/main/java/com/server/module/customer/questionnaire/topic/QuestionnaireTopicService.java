package com.server.module.customer.questionnaire.topic;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2019-01-25 16:01:45
 */
public interface QuestionnaireTopicService {


    public ReturnDataUtil listPage(QuestionnaireTopicCondition condition);

    public List<QuestionnaireTopicBean> list(QuestionnaireTopicCondition condition);

    public boolean update(QuestionnaireTopicBean entity);

    public boolean del(Long id);

    public QuestionnaireTopicBean get(Object id);

    public QuestionnaireTopicBean add(QuestionnaireTopicBean entity);

    List<TopicVO> listTopic(QuestionnaireTopicCondition questionnaireTopicCondition);
}

