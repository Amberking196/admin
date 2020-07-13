package com.server.module.customer.questionnaire.option;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2019-01-25 16:04:27
 */
public interface QuestionnaireOptionService {


    public ReturnDataUtil listPage(QuestionnaireOptionCondition condition);

    public List<QuestionnaireOptionBean> list(QuestionnaireOptionCondition condition);

    public boolean update(QuestionnaireOptionBean entity);

    public boolean del(Long id);

    public QuestionnaireOptionBean get(Object id);

    public QuestionnaireOptionBean add(QuestionnaireOptionBean entity);

    public List<QuestionnaireOptionBean> listOptionsByTopicId(Long topicId);



}

