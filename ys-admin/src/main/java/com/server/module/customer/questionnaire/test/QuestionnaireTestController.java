package com.server.module.customer.questionnaire.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;

import java.util.Date;

/**
 * author name: yjr
 * create time: 2019-01-25 17:02:19
 */
@Api(value = "QuestionnaireTestController", description = "问卷")
@RestController
@RequestMapping("/questionnaireTest")
public class QuestionnaireTestController {


    @Autowired
    private QuestionnaireTestService questionnaireTestServiceImpl;

    @ApiOperation(value = "问卷列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(QuestionnaireTestCondition condition) {
        return questionnaireTestServiceImpl.listPage(condition);
    }

    @ApiOperation(value = "问卷添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(@RequestBody QuestionnaireTestBean entity) {
        entity.setCreateTime(new Date());
        return new ReturnDataUtil(questionnaireTestServiceImpl.add(entity));
    }

    @ApiOperation(value = "添加问题", notes = "addTopics", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/addTopics", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil addTopics(Long testId,Long[] topics) {
    	System.out.println("-----");
    	System.out.println(testId);
    	System.out.println(topics.length);
        questionnaireTestServiceImpl.addTopics(testId,topics);
        return new ReturnDataUtil();
    }



    @ApiOperation(value = "问卷修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody QuestionnaireTestBean entity) {
        return new ReturnDataUtil(questionnaireTestServiceImpl.update(entity));
    }

    @ApiOperation(value = "问卷删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil del(Object id) {
        return new ReturnDataUtil(questionnaireTestServiceImpl.del(id));
    }
}

