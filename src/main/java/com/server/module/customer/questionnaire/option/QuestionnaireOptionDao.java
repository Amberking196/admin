package com.server.module.customer.questionnaire.option;

import java.util.List;

import com.server.module.customer.questionnaire.Option;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2019-01-25 16:04:27
 */
public interface QuestionnaireOptionDao {

    public ReturnDataUtil listPage(QuestionnaireOptionCondition condition);

    public List<QuestionnaireOptionBean> list(QuestionnaireOptionCondition condition);

    public boolean update(QuestionnaireOptionBean entity);

    public boolean delete(Long id);

    public QuestionnaireOptionBean get(Object id);

    public QuestionnaireOptionBean insert(QuestionnaireOptionBean entity);

    public List<QuestionnaireOptionBean> listOptionsByTopicId(Long topicId);

}

