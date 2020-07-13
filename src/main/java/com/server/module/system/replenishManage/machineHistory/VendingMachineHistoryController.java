package com.server.module.system.replenishManage.machineHistory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.common.utils.excel.ExportExcel;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.util.DateUtils;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: yjr
 * create time: 2018-11-06 14:32:26
 */
@Api(value = "VendingMachineHistoryController", description = "机器商品日志")
@RestController
@RequestMapping("/vendingMachineHistory")
public class VendingMachineHistoryController {

	@Autowired
	AdminUserService adminUserService;
    @Autowired
    private VendingMachineHistoryService vendingMachineHistoryServiceImpl;

    @ApiOperation(value = "机器商品日志列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(@RequestBody(required=false) VendingMachineHistoryCondition condition,HttpServletRequest request) {
    	if(condition==null) {
    		condition=new VendingMachineHistoryCondition();
    	}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		condition.setAreaId(user.getAreaId());
        return vendingMachineHistoryServiceImpl.listPage(condition);
    }
    @ApiOperation(value = "每天销售补货报表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    public void export(VendingMachineHistoryCondition condition, HttpServletResponse response,HttpServletRequest request) {
        condition.setPageSize(100000);
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		condition.setAreaId(user.getAreaId());
        ReturnDataUtil data = vendingMachineHistoryServiceImpl.listPage(condition);
        try {
            List<VendingMachineHistoryBean> list = (ArrayList<VendingMachineHistoryBean>) data.getReturnObject();
            String fileName = "每天销售补货报表" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			if(condition.getStartDay()!=null && condition.getEndDay()!=null) {
				//导出日志内容按时间格式导出
				bean.setContent("用户: "+bean.getOperatorName()+" 导出"+condition.getStartDay()+"--"+condition.getEndDay()+"每天销售补货报表的数据");
			}else {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出每天销售补货报表--当前页的数据");
			}
            new ExportExcel("每天销售补货报表", VendingMachineHistoryBean.class).setDataList(list)
                    .write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*@ApiOperation(value = "机器商品日志添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(@RequestBody VendingMachineHistoryBean entity) {
        return new ReturnDataUtil(vendingMachineHistoryServiceImpl.add(entity));
    }

    @ApiOperation(value = "机器商品日志修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody VendingMachineHistoryBean entity) {
        return new ReturnDataUtil(vendingMachineHistoryServiceImpl.update(entity));
    }

    @ApiOperation(value = "机器商品日志删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil del(Object id) {
        return new ReturnDataUtil(vendingMachineHistoryServiceImpl.del(id));
    }
*/

}

