package com.server.module.system.machineManage.machinesWayItem;

import java.io.IOException;
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
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.util.DateUtils;
import com.server.util.ReturnDataUtil;
import com.server.util.client.SmsClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: yjr
 * create time: 2018-08-31 14:03:10
 */
@Api(value = "VendingMachinesWayItemController", description = "货道商品")
@RestController
@RequestMapping("/vendingMachinesWayItem")
public class VendingMachinesWayItemController {


    @Autowired
    private VendingMachinesWayItemService vendingMachinesWayItemServiceImpl;
	@Autowired
	AdminUserService adminUserService;
	
    @ApiOperation(value = "货道商品列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(VendingMachinesWayItemCondition condition) {
        return vendingMachinesWayItemServiceImpl.listPage(condition);
    }
    @ApiOperation(value = "货道商品列表", notes = "listPageForReplenish", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/listPageForReplenish", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPageForReplenish(@RequestBody(required=false) VendingMachinesWayItemCondition condition,HttpServletRequest request){
    	if(condition==null) {
    		condition=new VendingMachinesWayItemCondition();
    		 if(condition.getType()==null)
        		 condition.setType(1);
    	}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if(user.getAreaId()!=null && user.getAreaId()>0) {
			condition.setAreaId(user.getAreaId());
		}
    	ReturnDataUtil returnDataUtil = vendingMachinesWayItemServiceImpl.listPageForReplenish(condition);
    	 return returnDataUtil;
    }
    @ApiOperation(value = "货道商品列表", notes = "expertForReplenish", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/expertForReplenish", produces = "application/json;charset=UTF-8")
    public void expertForReplenish(VendingMachinesWayItemCondition condition,HttpServletResponse response,HttpServletRequest request){
         condition.setPageSize(10000);
 		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
 		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if(user.getAreaId()!=null && user.getAreaId()>0) {
			condition.setAreaId(user.getAreaId());
		}
         ReturnDataUtil data= vendingMachinesWayItemServiceImpl.listPageForReplenish(condition);
         List<ReplenishVo> list=(List<ReplenishVo>)data.getReturnObject();
         
         for(ReplenishVo vo : list){
        	 
        	 List<ReplenishItemVo> listitem=vo.getItemList();
        	 StringBuilder sb=new StringBuilder();
        	 
        	 for(ReplenishItemVo obj : listitem){
        		 //肇鼎山泉  1∼2/20  18  90%
        		 sb.append(obj.getSimpleName()).append(" ").append(obj.getNum()).append("~")
        		 .append(obj.getNum()).append("/").append(obj.getFullNum()).append(" ").append(obj.getReplenishNum())
        		 .append(" ").append(obj.getPercent()).append("%  ").append(obj.getOutQuantity()).append("  ");
        	 }
        	 vo.setDetail(sb.toString());
         }
		//添加导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		bean.setContent("用户: "+bean.getOperatorName()+" 导出商品缺货管理--全部数据");
         String fileName = "缺货列表" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			try {
				new ExportExcel("缺货列表", ReplenishVo.class).setDataList(list)
						.write(response, fileName).dispose();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    
         
    }
    @ApiOperation(value = "货道商品添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(@RequestBody VendingMachinesWayItemBean entity) {
        return vendingMachinesWayItemServiceImpl.add(entity);
    }

    @ApiOperation(value = "货道商品修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody WayItemForUpdateVo vo) {
        return vendingMachinesWayItemServiceImpl.update(vo);
    }

    @ApiOperation(value = "货道商品删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil del(Long id) {
        return vendingMachinesWayItemServiceImpl.del(id);
    }
    
    @ApiOperation(value = "货道商品校准", notes = "sendOnceReviseTest", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/sendOnceReviseTest", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil sendOnceReviseTest(@RequestBody ReviseForm form) {
    	form.setUserId(UserUtils.getUser().getId());
    	return SmsClient.sendOnceReviseTest(form);
    }

    @ApiOperation(value = "修改视觉机器的最大容量", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/edit", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil edit(@RequestBody VendingMachinesWayItemBean bean) {
        return vendingMachinesWayItemServiceImpl.edit(bean);
    }
}

