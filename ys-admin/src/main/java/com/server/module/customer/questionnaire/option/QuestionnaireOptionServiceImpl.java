package com.server.module.customer.questionnaire.option;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2019-01-25 16:04:27
 */
@Service
public class QuestionnaireOptionServiceImpl implements QuestionnaireOptionService {

    private static Log log = LogFactory.getLog(QuestionnaireOptionServiceImpl.class);
    @Autowired
    private QuestionnaireOptionDao questionnaireOptionDaoImpl;

    public ReturnDataUtil listPage(QuestionnaireOptionCondition condition) {
        return questionnaireOptionDaoImpl.listPage(condition);
    }

    public QuestionnaireOptionBean add(QuestionnaireOptionBean entity) {
        return questionnaireOptionDaoImpl.insert(entity);
    }

    public boolean update(QuestionnaireOptionBean entity) {
        return questionnaireOptionDaoImpl.update(entity);
    }

    public boolean del(Long id) {
        return questionnaireOptionDaoImpl.delete(id);
    }

    public List<QuestionnaireOptionBean> list(QuestionnaireOptionCondition condition) {
        return null;
    }

    public QuestionnaireOptionBean get(Object id) {
        return questionnaireOptionDaoImpl.get(id);
    }

    public List<QuestionnaireOptionBean> listOptionsByTopicId(Long topicId){
        return questionnaireOptionDaoImpl.listOptionsByTopicId(topicId);
    }

}

