package com.server.module.customer.questionnaire.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2019-01-25 17:02:19
 */
@Service
public class QuestionnaireTestServiceImpl implements QuestionnaireTestService {

    private static Log log = LogFactory.getLog(QuestionnaireTestServiceImpl.class);
    @Autowired
    private QuestionnaireTestDao questionnaireTestDaoImpl;

    public ReturnDataUtil listPage(QuestionnaireTestCondition condition) {
        return questionnaireTestDaoImpl.listPage(condition);
    }

    public QuestionnaireTestBean add(QuestionnaireTestBean entity) {
        return questionnaireTestDaoImpl.insert(entity);
    }

    public boolean update(QuestionnaireTestBean entity) {
        return questionnaireTestDaoImpl.update(entity);
    }

    public boolean del(Object id) {
        return questionnaireTestDaoImpl.delete(id);
    }

    public List<QuestionnaireTestBean> list(QuestionnaireTestCondition condition) {
        return null;
    }

    public QuestionnaireTestBean get(Object id) {
        return questionnaireTestDaoImpl.get(id);
    }
    public void  addTopics(Long testId, Long[] topics){
         questionnaireTestDaoImpl.addTopics(testId,topics);
    }


}

