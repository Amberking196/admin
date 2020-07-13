package com.server.module.customer.questionnaire;

import com.server.util.ReturnDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuestionnaireService {
    @Autowired
    private QuestionnaireDao dao;
    public Test getTest(Integer type){
        Test test=dao.getTest(type);
        List<Topic> topicList=dao.getAllTopic(test.getId());

        for (int i = 0; i < topicList.size(); i++) {
            Topic topic=topicList.get(i);
            List<Option> optionList=dao.getAllOption(topic.getId());
            topic.setList(optionList);
        }
        test.setList(topicList);
        return test;
    }
    public Test showTestResult(Integer id){
        Test test=dao.getTestById(id);
        List<Topic> topicList=dao.getAllTopic(test.getId());
        Map<Integer,Integer> map=dao.getOptionAndAnswer(id);
        for (int i = 0; i < topicList.size(); i++) {
            Topic topic=topicList.get(i);
            List<Option> optionList=dao.getAllOption(topic.getId());
            for (Option option : optionList) {
                System.out.println("optionId="+option.getId()+"  "+map.get(option.getId()));
                if(map.get(option.getId())!=null)
                   option.setAnswerCount(map.get(option.getId()));
                else
                    option.setAnswerCount(0);
            }
            topic.setList(optionList);
        }
        test.setList(topicList);
        return test;
    }
    public void saveAnswer(Answer answer){
        dao.saveAnswer(answer);
    }


    public ReturnDataUtil getUserPage(DashboardCondition condition){
        return dao.getUserPage(condition);
    }
}
