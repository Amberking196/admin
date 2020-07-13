package com.server.module.system.adminUser;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.server.jwt.TokenAuthenticationService;
import com.server.module.commonBean.LoginInfoBean;
import com.server.module.system.adminMenu.AdminMenuBean;
import com.server.module.system.adminMenu.AdminMenuService;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.synthesizeManage.roleManage.RoleBean;
import com.server.module.system.synthesizeManage.roleManage.RoleManageService;
import com.server.util.RequestStr;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统用户控制层模块
 *
 * @author denfer
 */
@RestController
@RequestMapping("/adminUser")
@Api(value = "adminUserController", description = "员工信息操作")
public class AdminUserController {

    public static Logger log = LogManager.getLogger(AdminUserController.class);
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private AdminMenuService adminMenuService;
    @Autowired
    private ReturnDataUtil returnDataUtil;
    @Autowired
    private RequestStr requestStr;
    @Autowired
    private TokenAuthenticationService tokenAutheticationService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private RoleManageService roleManageService;


    @RequestMapping(value = "/test", method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public String test() {
        log.info("AdminUserrController---------test------ start:");
        log.info("AdminUserrController---------test------ end");
        return "ddd";
    }

    /***
     * 系统用户登录
     * @param userBean
     * @return
     */
    @ApiOperation(value = "系统用户登录", notes = "系统用户登录", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/adminUserLogin", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public ReturnDataUtil adminUserLogin(@RequestBody AdminUserForm user, HttpServletRequest request) {
        log.info("AdminUserrController---------adminUserLogin------ start");
        ReturnDataUtil returnData = new ReturnDataUtil();
        String userName = user.getLoginCode();
        String password = user.getPassword();
        returnData = adminUserService.adminUserLogin(userName, password);
        if (returnData.getStatus() == 1) {
            AdminUserBean loginUser = (AdminUserBean) returnData.getReturnObject();
            String param = loginUser.getId().toString() + "," + loginUser.getCompanyId();
            String token = tokenAutheticationService.generateToken(param);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("userName", loginUser.getName());
            data.put("companyId", loginUser.getCompanyId());
            data.put("token", token);
            //得到当前用户所属公司信息  
            CompanyBean companyBean = companyService.findCompanyById(loginUser.getCompanyId());
            data.put("logoPic", companyBean.getLogoPic());
            //判断当前登录用户所属公司 是否有子公司
          boolean checkIsSubsidiaries = companyService.checkIsSubsidiaries(loginUser.getCompanyId());
            if(checkIsSubsidiaries) {
            	data.put("isSubsidiaries", 1);
            }else {
            	data.put("isSubsidiaries", 0);
            }
            returnData.setReturnObject(data);
        }
        log.info("AdminUserrController---------adminUserLogin------ end");
        return returnData;
    }

    /**
     * 登录初始化接口
     *
     * @return
     * @author hebiting
     * @date 2018年4月12日上午10:12:42
     */
    @ApiOperation(value = "登录初始化接口", notes = "登录初始化接口", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/initInfo", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public ReturnDataUtil initInfo(HttpServletRequest request) {
        log.info("AdminUserrController---------initInfo------ start");
        ReturnDataUtil returnData = new ReturnDataUtil();
        Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
        AdminUserBean loginUser = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
        String role = loginUser == null ? null : loginUser.getRole();
        List<AdminMenuBean> menuList = adminMenuService.formatFindMenuByRole(role);
        if (menuList != null && menuList.size() > 0) {
            returnData.setStatus(1);
            returnData.setMessage("初始化成功，这里是开发调试环境！！！");
            returnData.setReturnObject(menuList);
        } else {
            returnData.setStatus(0);
            returnData.setMessage("初始化失败");
            returnData.setReturnObject(null);
        }
        log.info("AdminUserrController---------initInfo------ end");
        return returnData;
    }

    /**
     * 退出系统
     *
     * @return
     * @author hebiting
     * @date 2018年4月12日上午9:58:56
     */
    @ApiOperation(value = "退出系统", notes = "退出系统", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/logout")
    public ReturnDataUtil logout() {
        log.info("AdminUserrController---------logout------ start");
        ReturnDataUtil returnData = new ReturnDataUtil();
        returnData.setStatus(-1);
        returnData.setMessage("退出成功");
        log.info("AdminUserrController---------logout------ end");
        return returnData;
    }

    /**
     * 系统用户修改密码
     *
     * @param adminForm
     * @return ReturnDataUtil
     */
    @ApiOperation(value = "系统用户修改密码", notes = "系统用户修改密码", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public ReturnDataUtil updatPassword(@RequestBody AdminUserForm adminForm) {
        log.info("AdminUserrController---------updatPassword------ start");
        returnDataUtil = adminUserService.updatPassword(adminForm);
        log.info("AdminUserrController---------updatPassword------ end");
        return returnDataUtil;
    }

    /**
     * 用户个人修改密码
     *
     * @param adminForm
     * @return ReturnDataUtil
     */
    @ApiOperation(value = "用户个人修改密码", notes = "用户个人修改密码", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/updateUserPassword", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public ReturnDataUtil updatUserPassword(@RequestBody AdminUserForm adminForm, HttpServletRequest request) {
        log.info("AdminUserrController---------updatUserPassword------ start");
        ReturnDataUtil returnData = new ReturnDataUtil();
        Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
        adminForm.setId(userId);
        System.out.println(userId);
        AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
        Integer status = (Integer) adminUserService.adminUserLogin(user.getLoginCode(), adminForm.getPassword()).getStatus();
        if (status == 1 && StringUtil.isNotBlank(adminForm.getNewPassword())) {
            returnData = adminUserService.updatPassword(adminForm);
        } else {
            returnData.setStatus(0);
            returnData.setMessage("原密码输入错误");
        }
        log.info("AdminUserrController---------updatUserPassword------ end");
        return returnData;
    }

    /**
     * 获取部门用户列表
     *
     * @param departMent 部门
     * @return
     */
    @ApiOperation(value = "获取部门用户列表", notes = "获取部门用户列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/getAdminUserList", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public ReturnDataUtil getAdminUserList(@RequestBody Map<String, Integer> param) {
        log.info("AdminUserrController---------updatPassword------ start");
        returnDataUtil = adminUserService.getAdminUserList(param.get("department"), param.get("companyId"));
        log.info("AdminUserrController---------updatPassword------ end");
        return returnDataUtil;
    }


    /**
     * 添加员工信息
     *
     * @param user
     * @param session
     * @return
     */
    @ApiOperation(value = "添加员工信息", notes = "添加员工信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/createAdminUser")
    public ReturnDataUtil createAdminUser(@RequestBody AdminUserBean user, HttpServletRequest request) {
        log.info("AdminUserrController---------updateAdmimUser------ start");
        if (user != null && adminUserService.loginCodeIsOnlyOne(user.getLoginCode())) {
            Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
            user.setFounder(userId);
            returnDataUtil = adminUserService.createAdminUser(user);
        } else {
            returnDataUtil.setStatus(0);
            returnDataUtil.setMessage("用户名重复，创建失败");
        }
        log.info("AdminUserrController---------updateAdmimUser------ end");
        return returnDataUtil;
    }

    /**
     * 更新员工信息
     *
     * @param userBean
     * @return
     * @author hebiting
     * @date 2018年4月8日下午10:53:55
     */
    @ApiOperation(value = "更新员工信息", notes = "更新员工信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("updateAdminUser")
    public ReturnDataUtil updateAdmimUser(@RequestBody LoginInfoBean userBean) {
        log.info("AdminUserrController---------updateAdmimUser------ start");
        returnDataUtil = adminUserService.updateAdminUser(userBean);
        log.info("AdminUserrController---------updateAdmimUser------ end");
        return returnDataUtil;
    }

    /**
     * 根据公司id查询公司员工信息
     *
     * @param companyId
     * @return
     * @author hebiting
     * @date 2018年4月10日上午9:10:57
     */
    @ApiOperation(value = "根据公司id查询公司员工信息", notes = "根据公司id查询公司员工信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/findAdminUserByCompanyId")
    public ReturnDataUtil findAdminUserByCompanyId(@RequestBody(required = false) Map<String, Integer> param) {
        log.info("AdminUserrController---------findAdminUserByCompanyId------ start");
        returnDataUtil = adminUserService.findAllCompanyUser(param.get("companyId"));
        log.info("AdminUserrController---------findAdminUserByCompanyId------ end");
        return returnDataUtil;
    }

    /**
     * 根据条件查询用户信息
     *
     * @param userForm
     * @return
     * @author hebiting
     * @date 2018年4月10日下午4:57:11
     */
    @ApiOperation(value = "根据条件查询用户信息", notes = "根据条件查询用户信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/findUserByForm")
    public ReturnDataUtil findUserByForm(@RequestBody(required = false) SearchUserForm userForm, HttpServletRequest request) {
        log.info("AdminUserrController---------findUserByForm------ start");
        if (userForm == null) {
            userForm = new SearchUserForm();
        }
        if (userForm.getCompanyId() == null) {
            Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
            AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
            Integer companyId = user == null ? null : user.getCompanyId();
            List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
            String companyIds = StringUtils.join(companyList, ",");
            userForm.setCompanyIds(companyIds);
        }
        returnDataUtil = adminUserService.findUserByForm(userForm);
        log.info("AdminUserrController---------findUserByForm------ end");
        return returnDataUtil;
    }

    /**
     * 初始化查询信息
     *
     * @return
     * @author hebiting
     * @date 2018年4月10日下午5:09:29
     */
    @ApiOperation(value = "初始化查询信息", notes = "初始化查询信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/initSearchInfo")
    public ReturnDataUtil initSearchInfo(HttpServletRequest requeset) {
        log.info("AdminUserrController---------findUserByForm------ start");
        ReturnDataUtil returnData = new ReturnDataUtil();
        Long userId = (Long) requeset.getAttribute(AdminConstant.LOGIN_USER_ID);
        AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
        Integer companyId = user == null ? null : user.getCompanyId();
        List<CompanyBean> companyList = companyService.findAllSonCompany(companyId);
        List<DepartMentBean> departMentList = adminUserService.findAllDepartMent();
        if (companyList != null && departMentList != null && companyList.size() > 0 && departMentList.size() > 0) {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("companyList", companyList);
            data.put("departMentList", departMentList);
            returnData.setStatus(1);
            returnData.setMessage("初始化成功");
            returnData.setReturnObject(data);
        } else {
            returnData.setStatus(0);
            returnData.setMessage("初始化失败");
        }
        log.info("AdminUserrController---------findUserByForm------ end");
        return returnData;
    }


    /**
     * 初始化新增信息
     *
     * @return
     * @author hebiting
     * @date 2018年4月10日下午5:09:29
     */
    @ApiOperation(value = "初始化新增信息", notes = "初始化新增信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/initAddInfo")
    public ReturnDataUtil initAddInfo(HttpServletRequest requeset) {
        log.info("AdminUserrController---------initAddInfo------ start");
        ReturnDataUtil returnData = new ReturnDataUtil();
        Long userId = (Long) requeset.getAttribute(AdminConstant.LOGIN_USER_ID);
        AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
        //System.out.println("========================================"+ JSON.toJSONString(user));
        Integer companyId = user == null ? null : user.getCompanyId();
        List<CompanyBean> companyList = companyService.findAllSonCompany(companyId);
        String role = user.getRole();
        List<RoleBean> roleList=roleManageService.findSonRole(Integer.parseInt(role));
        if (companyList != null && companyList.size() > 0) {
            List<CompanyRoleDto> crList = new ArrayList<CompanyRoleDto>();
            CompanyRoleDto crDto = null;
            for (CompanyBean companyBean : companyList) {
                crDto = new CompanyRoleDto();
                crDto.setComapanyValue(companyBean);
                //crDto.setCompanyList(companyService.findAllSonCompany(companyBean.getId()));
                List<RoleBean> companyRoleList=(List<RoleBean>) roleManageService.findRoleByCompany(companyBean.getId()).getReturnObject();
                List<RoleBean> newList=Lists.newArrayList();
                if(companyRoleList!=null) {
					for(RoleBean bean:companyRoleList) {
	                	for(RoleBean rb:roleList) {
	                		if(rb.getId().equals(bean.getId())) {
	                			newList.add(bean);
	                		}
	                	}
	                }
                }
                crDto.setRoleList(newList);
                crList.add(crDto);
            }
            returnData.setStatus(1);
            returnData.setMessage("初始化成功");
            returnData.setReturnObject(crList);
        } else {
            returnData.setStatus(0);
            returnData.setMessage("初始化失败");
        }
        log.info("AdminUserrController---------initAddInfo------ end");
        return returnData;
    }


    /**
     * 判断登录名是否重复
     *
     * @param loginCode
     * @return
     * @author hebiting
     * @date 2018年4月24日下午3:57:47
     */
    @PostMapping("/isOnlyOne")
    public ReturnDataUtil isOnlyOne(@RequestBody Map<String, String> param) {
        log.info("AdminUserrController---------isOnlyOne------ start");


        ReturnDataUtil returnData = new ReturnDataUtil();

        // 用户名为空的情况
        if (param.get("loginCode")==null ||param.get("loginCode").equals("")  || param.get("loginCode").length() == 0) {
            returnData.setStatus(0);
            returnData.setMessage("用户名不能为空，请输入用户名");
            return returnData;
        }

        boolean result = adminUserService.loginCodeIsOnlyOne(param.get("loginCode"));
        if (result) {
            returnData.setStatus(1);
            returnData.setMessage("成功");
        } else {
            returnData.setStatus(0);
            returnData.setMessage("该登录帐号已被注册，请更换登录帐号");
        }
        log.info("AdminUserrController---------isOnlyOne------ end");
        return returnData;
    }

    /**
     * 判断手机号是否重复
     *
     * @param param
     * @return
     * @author hebiting
     * @date 2018年5月29日上午10:32:37
     */
    @PostMapping("/phoneRepeat")
    public ReturnDataUtil phoneRepeat(@RequestBody Map<String, String> param) {
        log.info("AdminUserrController---------phoneRepeat------ start");
        ReturnDataUtil returnData = new ReturnDataUtil();
        boolean result = true;
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(param.get("phone"));
        if (matcher.matches()) {
            result = adminUserService.phoneRepeat(param.get("phone"));
        }
        if (!result) {
            returnData.setStatus(1);
            returnData.setMessage("成功");
        } else {
            returnData.setStatus(0);
            returnData.setMessage("该手机号已被注册或输入号码不正常，请更换手机号");
        }
        log.info("AdminUserrController---------phoneRepeat------ end");
        return returnData;
    }

    /**
     * 根据公司查询出当前公司下所有的区域
     *
     * @param param
     * @return
     * @author zhongfucheng
     * @date 2018年7月21日09:12:22
     */
    @PostMapping("/findAreaByCompanyId")
    public ReturnDataUtil findAreaByCompanyId(@RequestBody Map<String, String> param) {
        log.info("AdminUserrController---------findAreaByCompanyId------ start");

        String level = param.get("level");
        String companyId = param.get("companyId");

        ReturnDataUtil returnData = new ReturnDataUtil();


        // 如果员工为2或者3都可以查询出公司下的区域...
        if (level.equals("2") || level.equals("3")) {
            // 根据公司Id查找出公司下的区域
            if (StringUtil.isNotBlank(level)  && StringUtil.isNotBlank(companyId)) {

                List<AdminAreaDto> areas = adminUserService.findAreaByCompanyId(companyId);
                returnData.setStatus(1);
                returnData.setMessage("成功");
                returnData.setReturnObject(areas);
            } else {
                returnData.setStatus(0);
                returnData.setMessage("公司Id为null/员工级别不为2或者3");
            }
        }

        log.info("AdminUserrController---------findAreaByCompanyId------ end");
        return returnData;
    }
    @PostMapping("/findAllAreaByCompanyId")
    public ReturnDataUtil findAllAreaByCompanyId(Integer companyId) {
    	log.info("AdminUserrController---------findAreaByCompanyId------ start");
    	ReturnDataUtil returnData = new ReturnDataUtil();
    	List<AdminAreaDto> areas = adminUserService.findAreaByCompanyId(companyId.toString());
    	returnData.setStatus(1);
    	returnData.setMessage("成功");
    	returnData.setReturnObject(areas);
    	log.info("AdminUserrController---------findAreaByCompanyId------ end");
    	return returnData;
    }

    /**
     * 根据区域查询出所有的线路
     *
     * @param param
     * @return
     * @author zhongfucheng
     * @date 2018年7月21日09:12:22
     */
    @PostMapping("/findLineByAreaAndCompanyId")
    public ReturnDataUtil findLineByAreaAndCompanyId(@RequestBody Map<String, String> param) {
        log.info("AdminUserrController---------findLineByAreaAndCompanyId------ start");

        String level = param.get("level");
        String areaId = param.get("areaId");
        String companyId = param.get("companyId");

        ReturnDataUtil returnData = new ReturnDataUtil();

        // 根据公司Id查找出公司下的区域
        if (StringUtil.isNotBlank(level) && level.equals("3") && StringUtil.isNotBlank(areaId) && StringUtil.isNotBlank(companyId)) {

            List<AdminLineDto> lines = adminUserService.findLineByAreaIdAndCompanyId(areaId, companyId);

            returnData.setStatus(1);
            returnData.setMessage("成功");
            returnData.setReturnObject(lines);
        } else {
            returnData.setStatus(0);
            returnData.setMessage("公司Id或者区域Id为null/员工级别不为3");
        }
        log.info("AdminUserrController---------findLineByAreaAndCompanyId------ end");
        return returnData;
    }
    
    
    @ApiOperation(value = "删除用户信息", notes = "删除用户信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/deleteUser")
    public ReturnDataUtil deleteUser(@RequestBody AdminUserBean adminUserBean , HttpServletRequest request) {
        log.info("AdminUserController---------deleteUser------ start");

        Boolean result=adminUserService.deleteAdminUser(adminUserBean);
        ReturnDataUtil returnDataUtil=new ReturnDataUtil(result);
        if(result){
        	returnDataUtil.setMessage("删除成功");
        }else {
        	returnDataUtil.setStatus(0);
        	returnDataUtil.setMessage("删除失败");
        }
        log.info("AdminUserController---------deleteUser------ end");
        return returnDataUtil;
    }

    //将售货机的某个货道绑定到某个员工
    @PostMapping("/addMachineToUser")
    public ReturnDataUtil addMachineToUser(@RequestBody AdminMachine machine, HttpServletRequest request) {
        log.info("AdminUserrController---------addMachineToUser------ start");
        ReturnDataUtil returnData = new ReturnDataUtil();
        List<AdminMachine> ways = adminUserService.getWayByUserIdAndVmcode(machine.getUserId(), machine.getVmCode());
        System.out.println("=============================================");
        System.out.println(ways);
        if (ways.size() > 0){
            AdminMachine t = new AdminMachine();
            for(int i=0;i<ways.size();i++){
                t = ways.get(i);
                System.out.println(t.getWay());
            }
                if (t.getWay() == machine.getWay()) {
                    returnData.setStatus(0);
                    returnData.setMessage("该售货机的该货道已被绑定");
                }
                else {
                    returnData = adminUserService.addMachineToUser(machine);

                }

        }else if (ways.size()<1){
            returnData.setStatus(1);
            returnData.setMessage("你怎么不绑定啊");
            returnData = adminUserService.addMachineToUser(machine);
        }
        log.info("AdminUserrController---------addMachineToUser------ end");
        return returnData;
    }

     //查询该员工已绑定的所有售货机
    @GetMapping("/getMachinesByUserId")
    public ReturnDataUtil getMachinesByUserId(Integer userId){
        List<AdminMachine> machine = adminUserService.getMachinesByUserId(userId);
        return new ReturnDataUtil(machine);
        //return new ReturnDataUtil(adminUserService.getMachinesByUserId(userId));
    }

    //查询该售货机的某货道是否被该员工绑定
    @GetMapping("/getWayByUserIdAndVmcode")
    public ReturnDataUtil getWayByUserIdAndVmcode(Integer userId,String vmCode){
        List<AdminMachine> way = adminUserService.getWayByUserIdAndVmcode(userId,vmCode);
        return new ReturnDataUtil(way);
    }

    @PostMapping("/delMachinesById")
    @ResponseBody
    public ReturnDataUtil delMachinesByUserId(@RequestBody List<Integer> mwIds) {
        return adminUserService.delMachinesByUserId(mwIds);
    }
}
