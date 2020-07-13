package com.server.module.customer.questionnaire.option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;

/**
 * author name: yjr
 * create time: 2019-01-25 16:04:27
 */
@Api(value = "QuestionnaireOptionController", description = "选项")
@RestController
@RequestMapping("/questionnaireOption")
public class QuestionnaireOptionController {


    @Autowired
    private QuestionnaireOptionService questionnaireOptionServiceImpl;

   /* @ApiOperation(value = "选项列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(QuestionnaireOptionCondition condition) {
        return questionnaireOptionServiceImpl.listPage(condition);
    }*/

    @ApiOperation(value = "选项添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(@RequestBody QuestionnaireOptionBean entity) {
        return new ReturnDataUtil(questionnaireOptionServiceImpl.add(entity));
    }

    @ApiOperation(value = "选项修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody QuestionnaireOptionBean entity) {
        return new ReturnDataUtil(questionnaireOptionServiceImpl.update(entity));
    }

    @ApiOperation(value = "选项删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil del(Long id) {
        return new ReturnDataUtil(questionnaireOptionServiceImpl.del(id));
    }



}

