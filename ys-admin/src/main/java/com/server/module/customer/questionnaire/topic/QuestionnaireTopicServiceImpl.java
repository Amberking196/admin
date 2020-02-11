package com.server.module.customer.questionnaire.topic;

import com.server.module.customer.questionnaire.option.QuestionnaireOptionBean;
import com.server.module.customer.questionnaire.option.QuestionnaireOptionDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2019-01-25 16:01:45
 */
@Service
public class QuestionnaireTopicServiceImpl implements QuestionnaireTopicService {

    private static Log log = LogFactory.getLog(QuestionnaireTopicServiceImpl.class);
    @Autowired
    private QuestionnaireTopicDao questionnaireTopicDaoImpl;
    @Autowired
    private QuestionnaireOptionDao questionnaireOptionDao;

    public ReturnDataUtil listPage(QuestionnaireTopicCondition condition) {
        ReturnDataUtil data= questionnaireTopicDaoImpl.listPage(condition);
        List<QuestionnaireTopicBean> list=(List<QuestionnaireTopicBean>)data.getReturnObject();
        for (QuestionnaireTopicBean obj : list) {
            Long id=obj.getId();
           List<QuestionnaireOptionBean> optionBeanList= questionnaireOptionDao.listOptionsByTopicId(id);
           obj.setOptionBeanList(optionBeanList);
        }

        return data;
    }

    public QuestionnaireTopicBean add(QuestionnaireTopicBean entity) {
        return questionnaireTopicDaoImpl.insert(entity);
    }

    public boolean update(QuestionnaireTopicBean entity) {
        return questionnaireTopicDaoImpl.update(entity);
    }

    public boolean del(Long id) {
        return questionnaireTopicDaoImpl.delete(id);
    }

    public List<QuestionnaireTopicBean> list(QuestionnaireTopicCondition condition) {
        return null;
    }

    public QuestionnaireTopicBean get(Object id) {
        return questionnaireTopicDaoImpl.get(id);
    }
    public List<TopicVO> listTopic(QuestionnaireTopicCondition questionnaireTopicCondition ){
        return questionnaireTopicDaoImpl.listTopic(questionnaireTopicCondition);
    }
}

