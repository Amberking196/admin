package com.server.module.system.officialManage.officialUser;

import com.alibaba.fastjson.JSON;
import com.server.jwt.TokenAuthenticationService;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制层模块
 * @author HHH
 */
@RestController
@RequestMapping("/OfficialUser")
@Api(value = "officialUserController", description = "用户注册登录")
public class OfficialUserController {
    public static Logger log = LogManager.getLogger(OfficialUserController.class);

    @Autowired
    private OfficialUserService officialUserService;
    @Autowired
    private ReturnDataUtil returnDataUtil;
    @Autowired
    private TokenAuthenticationService tokenAutheticationService;


    /**
     * 注册用户
     *
     * @param officialUser
     * @param
     * @return
     */
    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/createOfficialUser")
    public ReturnDataUtil  createOfficialUser( @RequestBody OfficialUserBean officialUser,HttpServletRequest request) {
        ReturnDataUtil returnData = new ReturnDataUtil();
        if (officialUser == null) {
            returnData.setStatus(0);
            returnData.setMessage("用户数据实体映射失败，请检查是否缺失请求参数！");
            return returnData;
        }
        // 校验用户是否已存在，如果不符合，直接返回数据封装对象
         boolean userExits = officialUserService.loginCodeIsOnlyOne(officialUser.getLoginCode());
        // 根据用户账号查询用户
        //OfficialUserBean exitsUser = officialUserService.findUserByLoginCode(officialUser.getLoginCode());
        if (userExits == false) {
            returnData.setStatus(0);
            returnData.setMessage("用户已存在，不可重复注册！");
            return  returnData;
        }
        officialUser.setStatus(1);
        officialUser.setCompanyId(1);
        OfficialUserBean bean = officialUserService.createOfficialUser(officialUser);
       //获取到 Service 返回的业务数据对象，“塞”进数据封装对象
        returnData.setReturnObject(bean);
        returnData.setMessage("用户注册成功！");
        return  returnData;
    }

    /**
     * 用戶列表查询
     * @param userForm
     * @return
     */
    @ApiOperation(value = " 用戶列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(SearchOffUserForm userForm) {
        return officialUserService.listPage(userForm);
    }

    /***
     * 用户登录
     * @param user
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/officialUserLogin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ReturnDataUtil officialUserLogin(@RequestBody OfficialUserBean user, HttpServletRequest request) {
        log.info("OfficialUserController---------officialUserLogin------ startuuuuu");
        ReturnDataUtil returnData = new ReturnDataUtil();
        String loginCode = user.getLoginCode();
        String password = user.getPassword();
        returnData = officialUserService.officialUserLogin(loginCode, password);
        if (returnData.getStatus() == 1) {
            //根据登录ID生成token
            OfficialUserBean loginUser = (OfficialUserBean) returnData.getReturnObject();
            String param = loginUser.getId().toString() + "," + loginUser.getCompanyId();
            String offToken = tokenAutheticationService.generateToken(param);
            //把用户名和token 存起来
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("userName", loginUser.getName());
            data.put("companyId", loginUser.getCompanyId());
            data.put("token", offToken);
            System.out.println(offToken);
        }
        log.info("OfficialUserController---------officialUserLogin------ end");
        return returnData;
    }


    /**
     * 退出系统
     *
     * @return
     * @author
     */
    @ApiOperation(value = "退出登录", notes = "退出登录", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/logout")
    public ReturnDataUtil logout() {
        log.info("OfficialUserController---------logout------ start");
        //直接改了登录状态
        ReturnDataUtil returnData = new ReturnDataUtil();
        returnData.setStatus(-1);
        returnData.setMessage("退出成功");
        log.info("OfficialUserController---------logout------ end");
        return returnData;
    }


    /**
     * 用户个人修改密码
     *
     * @param officialForm
     * @return ReturnDataUtil
     */
    @ApiOperation(value = "用户修改密码", notes = "用户修改密码", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/updateUserPassword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ReturnDataUtil updatUserPassword(@RequestBody OfficialUserForm officialForm, HttpServletRequest request) {
        log.info("OfficialUserController---------updatUserPassword------ start");
        //获取当前登录用户的ID
        ReturnDataUtil returnData = new ReturnDataUtil();
        Long userId = (Long) request.getAttribute(OfficialConstant.LOGIN_OFF_ID);
        officialForm.setId(userId);
        //System.out.println("hhh" + userId);
        OfficialUserBean user = (OfficialUserBean) officialUserService.findUserById(userId).getReturnObject();
        Integer status = (Integer) officialUserService.officialUserLogin(user.getLoginCode(), officialForm.getPassword()).getStatus();
        if (status == 1 && StringUtil.isNotBlank(officialForm.getNewPassword())) {
            returnData = officialUserService.updatPassword(officialForm);
        } else {
            returnData.setStatus(0);
            returnData.setMessage("原密码输入错误");
        }
        log.info("OfficialUserController---------updatUserPassword------ end");
        return returnData;
    }

    /**
     * 预编译
     *
     * @return
     * @author hanhuanhuan
     * @date 2019年8月21日09:35:09
     */
    @ApiOperation(value = "初始化修改信息", notes = "初始化修改信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/preUpdateUser")
    public ReturnDataUtil preUpdateUser(Long userId,HttpServletRequest request) {
        log.info("OfficialUserController---------preUpdateUser------ start");
        ReturnDataUtil returnData = new ReturnDataUtil();
        Long loginId = (Long) request.getAttribute(OfficialConstant.LOGIN_OFF_ID);
        OfficialUserBean loginBean = (OfficialUserBean)officialUserService.findUserById(loginId).getReturnObject();
        Integer role = loginBean.getRole();
        //System.out.println(role);
        if (role != 1){
            returnData.setStatus(0);
            returnData.setMessage("您的权限等级不够");
        }else {
            returnData.setStatus(1);
            returnData.setMessage("初始化成功");
            OfficialUserBean userBean = (OfficialUserBean)officialUserService.findUserById(userId).getReturnObject();
            System.out.println(userBean);
        }
        log.info("OfficialUserController---------preUpdateUser------ end");
        return returnData;
    }

    /**
     * 更新用户信息
     *
     * @param userBean
     * @return
     * @author hhh
     * @date 2019年8月21日10:26:39
     */
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/updateUser")
    public ReturnDataUtil updateOffUser(@RequestBody OfficialUserBean userBean) {
        log.info("OfficialUserController---------updateOffUser------ start");
        returnDataUtil = officialUserService.updateOffUser(userBean);
        log.info("OfficialUserController---------updateOffUser------ end");
        return returnDataUtil;
    }
}
