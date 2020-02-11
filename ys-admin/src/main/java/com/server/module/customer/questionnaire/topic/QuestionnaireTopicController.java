package com.server.module.customer.questionnaire.topic;

import com.server.module.customer.questionnaire.option.QuestionnaireOptionBean;
import com.server.module.customer.questionnaire.option.QuestionnaireOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * author name: yjr
 * create time: 2019-01-25 16:01:45
 */
@Api(value = "QuestionnaireTopicController", description = "题目库")
@RestController
@RequestMapping("/questionnaireTopic")
public class QuestionnaireTopicController {


    @Autowired
    private QuestionnaireTopicService questionnaireTopicServiceImpl;
    @Autowired
    private QuestionnaireOptionService questionnaireOptionService;
    @ApiOperation(value = "题目库列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(QuestionnaireTopicCondition condition) {
        condition.setPageSize(50);
        return questionnaireTopicServiceImpl.listPage(condition);
    }

    @ApiOperation(value = "题目库添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(@RequestBody QuestionnaireTopicBean entity) {

        return new ReturnDataUtil(questionnaireTopicServiceImpl.add(entity));
    }

    @ApiOperation(value = "题目选项表", notes = "listOptions", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listOptions", produces = "application/json;charset=UTF-8")
    public List<QuestionnaireOptionBean> listOptions(Long topicId) {
        return questionnaireOptionService.listOptionsByTopicId(topicId);
    }

    @ApiOperation(value = "题目库修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody QuestionnaireTopicBean entity) {
        return new ReturnDataUtil(questionnaireTopicServiceImpl.update(entity));
    }

    @ApiOperation(value = "题目库删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil del(Long id) {
        return new ReturnDataUtil(questionnaireTopicServiceImpl.del(id));
    }

    @ApiOperation(value = "题目库列表", notes = "listTopic", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/listTopic", produces = "application/json;charset=UTF-8")
    public List<TopicVO> listTopic(@RequestBody(required=false) QuestionnaireTopicCondition questionnaireTopicCondition ) {
    	if(questionnaireTopicCondition==null) {
    		questionnaireTopicCondition=new QuestionnaireTopicCondition();
    	}
        return questionnaireTopicServiceImpl.listTopic(questionnaireTopicCondition);
    }


}

