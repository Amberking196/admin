package com.server.module.system.machineManage.machinesWayTemplate;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * author name: zfc
 * create time: 2018-08-03 10:19:25
 */
@Api(value = "VendingMachinesWayTemplateController", description = "售货机模板")
@RestController
@RequestMapping("/vendingMachinesWayTemplate")
public class VendingMachinesWayTemplateController {


    @Autowired
    private VendingMachinesWayTemplateService vendingMachinesWayTemplateServiceImpl;

    @Autowired
    private AdminUserService adminUserServiceImpl;


    @ApiOperation(value = "售货机模板添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/addTemplate", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil addTemplate(@RequestBody VendingMachinesWayTemplateBean entity, HttpServletRequest request) {

        // 模板名不能重复，先验证一下模板名
        boolean b = vendingMachinesWayTemplateServiceImpl.checkTemplateName(entity.getTemplateName());
        if (b) {
            ReturnDataUtil dataUtil = new ReturnDataUtil();
            dataUtil.setStatus(0);
            dataUtil.setMessage("模板名已重复，请换一个模板名");

            return dataUtil;
        } else {
            // 得到当前用户Id 查询出当前用户信息
            Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
            ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
            AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
            if (userBean.getAreaId() == 0) {
                userBean.setAreaId(null);
            }
            entity.setAreaID(userBean.getAreaId());
            entity.setCompanyId(userBean.getCompanyId());
            entity.setUserId(userId);
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());

            return new ReturnDataUtil(vendingMachinesWayTemplateServiceImpl.add(entity));
        }

    }



    @ApiOperation(value = "查询出个人的模板", notes = "listOwnTemplate", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listOwnTemplate", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listOwnTemplate(HttpServletRequest request, VendingMachinesWayTemplateCondition condition) {

        if (condition == null) {
            condition = new VendingMachinesWayTemplateCondition();
        }

        // 得到当前用户Id，根据用户Id查询出模板的信息
        Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
        condition.setPageSize(100000);
        condition.setUserId(userId);

        return vendingMachinesWayTemplateServiceImpl.listOwnTemplate(condition);
    }

    @ApiOperation(value = "售货机模板名称列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listTemplateName(HttpServletRequest request, VendingMachinesWayTemplateCondition condition) {

        if (condition == null) {
            condition = new VendingMachinesWayTemplateCondition();
        }

        // 得到当前用户Id 查询出当前用户信息
        Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
        ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
        AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
        if (userBean.getAreaId() == 0) {
            userBean.setAreaId(null);
        }
        condition.setPageSize(100000);
        condition.setCompanyId(userBean.getCompanyId());
        condition.setAreaId(userBean.getAreaId());

        return vendingMachinesWayTemplateServiceImpl.listTemplateName(condition);
    }


    @ApiOperation(value = "售货机模板列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listTemplatePage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listTemplatePage(HttpServletRequest request,VendingMachinesWayTemplateCondition condition) {

        if (condition == null) {
            condition = new VendingMachinesWayTemplateCondition();
        }

        // 得到当前用户Id 查询出当前用户信息
        Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
        ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);

        AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
        if (userBean.getAreaId() == 0) {
            userBean.setAreaId(null);
        }
        condition.setCompanyId(userBean.getCompanyId());
        condition.setAreaId(userBean.getAreaId());

        return vendingMachinesWayTemplateServiceImpl.listPage(condition);
    }



    @ApiOperation(value = "根据模板Id查询出详情(模板内的数据)", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listDetailsById/{templateId}", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listDetailsById(@PathVariable("templateId") Long templateId) {

       return vendingMachinesWayTemplateServiceImpl.listDetailsById(templateId);
    }


    @ApiOperation(value = "售货机模板修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody VendingMachinesWayTemplateBean entity) {
        return new ReturnDataUtil(vendingMachinesWayTemplateServiceImpl.update(entity));
    }

    @ApiOperation(value = "售货机模板删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil del( @RequestBody VendingMachinesWayTemplateBean bean ) {
        return new ReturnDataUtil(vendingMachinesWayTemplateServiceImpl.del(bean));
    }



}

