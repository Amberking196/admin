package com.server.module.customer.questionnaire.test;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2019-01-25 17:02:19
 */
public interface QuestionnaireTestDao {

    public ReturnDataUtil listPage(QuestionnaireTestCondition condition);

    public List<QuestionnaireTestBean> list(QuestionnaireTestCondition condition);

    public boolean update(QuestionnaireTestBean entity);

    public boolean delete(Object id);

    public QuestionnaireTestBean get(Object id);

    public QuestionnaireTestBean insert(QuestionnaireTestBean entity);

    public void addTopics(Long testId, Long[] topics);
}

