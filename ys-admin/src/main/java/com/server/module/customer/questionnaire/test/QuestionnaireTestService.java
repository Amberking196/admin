package com.server.module.customer.questionnaire.test;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2019-01-25 17:02:19
 */
public interface QuestionnaireTestService {


    public ReturnDataUtil listPage(QuestionnaireTestCondition condition);

    public List<QuestionnaireTestBean> list(QuestionnaireTestCondition condition);

    public boolean update(QuestionnaireTestBean entity);

    public boolean del(Object id);

    public QuestionnaireTestBean get(Object id);

    public QuestionnaireTestBean add(QuestionnaireTestBean entity);

    public void  addTopics(Long testId, Long[] topics);
}

