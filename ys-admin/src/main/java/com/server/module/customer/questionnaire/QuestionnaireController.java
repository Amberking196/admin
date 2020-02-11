package com.server.module.customer.questionnaire;

import com.google.common.collect.Lists;
import com.server.util.JsonUtil;
import com.server.util.JsonUtils;
import com.server.util.client.SmsClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Api(value = "QuestionnaireController", description = "调研")
@Controller
//@RequestMapping("/questionnaire")
public class QuestionnaireController {
    @Autowired
    private QuestionnaireService questionnaireService;
    @RequestMapping(value="/questionnaire")
    public String index(Model model){
        System.out.println("questionnaire===");
        //SmsClient.sendMessage("13570339593","【优水到家】 请你为我们做个问卷调查，有着数，谢谢！ 请点击 http://192.168.0.132:6663/questionnaire");
        Test test=questionnaireService.getTest(1);
        System.out.println(JsonUtils.toJson(test));
        model.addAttribute("test",test);
        return "questionnaire";
    }
    @RequestMapping(value="/questionnaire/result")
    public String result(Model model,Integer testId){
        System.out.println("questionnaire===");
        Test test=questionnaireService.showTestResult(testId);
        System.out.println(JsonUtils.toJson(test));
        model.addAttribute("test",test);
        return "questionnaire_result";
    }

    @RequestMapping(value="/questionnaire/submit")
    public String submit(Model model, HttpServletRequest request){
        System.out.println("questionnaire===submit==");
        String userId=request.getParameter("userId");
        String testId=request.getParameter("testId");
        String[] topics=request.getParameterValues("topicIds");
        String suggest=request.getParameter("suggest");
        Integer user=Integer.parseInt(userId);
        Integer test=Integer.parseInt(testId);
        Answer answer=new Answer();
        answer.setSuggest(suggest);
        answer.setUserId(user);
        answer.setTestId(test);
        List<AnswerItem> itemList= Lists.newArrayList();
        for (String topic : topics) {
            String option=request.getParameter("topic_"+topic);
            System.out.println("toipc id="+topic+" option id="+option);
            AnswerItem item=new AnswerItem();
            Integer optionId=Integer.parseInt(option);
            Integer topicId=Integer.parseInt(topic);
            item.setOptionId(optionId);
            item.setTopicId(topicId);
            itemList.add(item);
        }
        answer.setItemList(itemList);
        questionnaireService.saveAnswer(answer);
        return "questionnaire_success";
    }

    @RequestMapping(value="/questionnaire/send")
    @ResponseBody
    public Object sendSms(){


        return null;
    }
    @ApiOperation(value = "待发送用户列表", notes = "getUserPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value="/questionnaire/getUserPage")
    @ResponseBody
    public Object getUserPage(DashboardCondition condition){
        return questionnaireService.getUserPage(condition);
    }

    @ApiOperation(value = "用户分群状态列表", notes = "getUserTypeMap", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value="/questionnaire/getUserTypeMap")
    @ResponseBody
    public Object getUserTypeMap(){
        return QuestionnaireUtils.getUserTypeMap();
    }

    @ApiOperation(value = "发送状态列表", notes = "getSendFlagMap", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value="/questionnaire/getSendFlagMap")
    @ResponseBody
    public Object getSendFlagMap(){
        return QuestionnaireUtils.getSendFlagMap();
    }


}
