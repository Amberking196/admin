package com.server.module.system.shoppingManager.customerOrder;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.server.module.app.home.ResultEnum;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.companyManage.CompanyServiceImpl;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.userManage.CustomerController;
import com.server.module.system.warehouseManage.warehouseRemoval.WarehouseRemovalForm;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillBean;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillForm;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ExportExcel;
import com.server.util.HttpUtil;
import com.server.util.JsonUtils;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.SmsSendRequest;
import com.server.util.SmsSendResponse;
import com.server.util.SmsSendUtil;
import com.server.util.StringUtil;
import com.server.util.client.SmsClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/CustomerOrder")
@Api(value = "CustomerController", description = "商城订单的查询")

public class CustomerOrderController {

    public static Logger log = LogManager.getLogger(CustomerController.class);

    @Autowired
    CustomerOrderService orderService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private CompanyService companyService;

    
	@Value("${spring.profiles.active}")
	private String active;
    /**
     * 商城订单的查询
     *
     * @param orderForm
     * @return
     */
    @ApiOperation(value = "商城订单的查询", notes = "商城订单的查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/findCustomerOrder")
    @ResponseBody
    public ReturnDataUtil findCustomerByForm(@RequestBody(required = false) CustomerOrderForm orderForm, HttpServletRequest request) {
    	log.info("<CustomerOrderController>------<findCustomerByForm>-----start");
    	if (orderForm == null) {
            orderForm = new CustomerOrderForm();
        }
    	log.info("<CustomerOrderController>------<findCustomerByForm>-----end");
        return orderService.findCustomerByForm(orderForm);

    }
	
	/**
	 * 用户商城订单导出
	 * 
	 * @param response
	 * @param warehouseOutputBillForm
	 */
	@ApiOperation(value = "用户商城订单导出", notes = "用户商城订单导出")
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/export")
	public void exportFile(HttpServletResponse response,HttpServletRequest request, CustomerOrderForm orderForm) {
		log.info("<CustomerOrderController>------<exportFile>-----start-----用户商城订单导出");
		ReturnDataUtil data1 = orderService.findCustomerByForm(orderForm);
		orderForm.setPageSize(data1.getPageSize() * data1.getTotalPage());
		ReturnDataUtil returnData = orderService.findCustomerByForm(orderForm);
		List<CustomerOrderBean> data = (List<CustomerOrderBean>) returnData.getReturnObject();
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		ExportExcel exportExcel = new ExportExcel(wb, sheet);
		// 标题
		String title = "商城订单列表";
		String[] headers = new String[] { "num", "orderName", "payName", "stateName", "time", "payCode",
				"payTime", "receiver", "consigneePhone","location" };
		String[] column = new String[] { "序号", "订单类型", "支付类型", "订单状态", "创建时间", "内部订单号", "支付时间", "收货人", 
				"收货人手机","收货地址"};
		
		String[] headers2 = new String[] { "itemName","num","price","phone","totalPrice"};
		String[] column2 = new String[] {"商品名称","数量","价格","用户手机","总价"};
		// 创建报表头部
		int length=column.length+column2.length;
		exportExcel.createNormalShoppingGoodsHead(title, length);
		try {
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			if(orderForm.getStartDate()!=null&&orderForm.getEndDate()!=null) {
				//导出日志内容时间格式输出
				bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(orderForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(orderForm.getEndDate())+"的商城订单列表的数据");
			}else {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出商城订单列表--全部数据");
				
			}
			exportExcel.createNormalShoppingGoodsFiveRow(data, column, headers,column2, headers2);
			exportExcel.outputExcel(title, response);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("<CustomerOrderController>------<exportFile>-----end");
	}

    /**
     * 根据条件统计出用户的消费记录
     */
    @ApiOperation(value = "根据条件统计出用户的消费记录", notes = "根据条件统计出用户的消费记录", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/statisticsConsumptionRecord")
    @ResponseBody
    public ReturnDataUtil statisticsConsumptionRecord(@RequestBody(required = false) UsersConsumptionRecordForm form, HttpServletRequest request) {
        if (form == null) {
            form = new UsersConsumptionRecordForm();
        }
        Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
        AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
        // 如果没有给出具体的公司，就查询子公司的
        if(form.getCompanyId()==null){
            Integer companyId = user==null? null:user.getCompanyId();
            List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
            String companyIds =  StringUtils.join(companyList, ",");
            form.setCompanyIds(companyIds);
        }
		String[] roles = user.getRole().split(",");
		List<String> rolesList = Arrays.asList(roles);
		ReturnDataUtil returnDataUtil=orderService.statisticsConsumptionRecord(form);
//		//超级管理/总部运营/产品经理
//        if(rolesList.contains("9") || rolesList.contains("39") || rolesList.contains("90")) {
//        }else {
//        	returnDataUtil.setStatus(0);
//        }
        return returnDataUtil;

    }
    
    /**
     * 给全部用户发送短信
     */
    @ApiOperation(value = "给全部用户发送短信", notes = "给全部用户发送短信", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/sendAllMessage")
    @ResponseBody
    public ReturnDataUtil sendAllMessage(@RequestBody(required = false) UsersConsumptionRecordForm form,HttpServletRequest request) {
		log.info("<CustomerOrderController>--<sendAllMessage>--start");
		log.info("当前系统模式为"+active);
    	ReturnDataUtil returnDataUtil=new ReturnDataUtil();
        if (form == null) {
            form = new UsersConsumptionRecordForm();
        }
        // 如果没有给出具体的公司，就查询子公司的
        Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
        AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
        if(form.getCompanyId()==null){
            Integer companyId = user==null? null:user.getCompanyId();
            List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
            String companyIds =  StringUtils.join(companyList, ",");
            form.setCompanyIds(companyIds);
        }
        form.setIsShowAll(1);    
        List<UsersConsumptionRecordVo> list=(List<UsersConsumptionRecordVo>) orderService.statisticsConsumptionRecord(form).getReturnObject();
   		String phoneList="";int t=1;

   		int time= list.size()%900==0?list.size()/900:list.size()/900+1;
   		log.info("发送"+time+"次 size"+list.size());
        //非开发模式 
        if(!active.equals("dev")) {
    		for(int i=1;i<=list.size();i++) {
    			if(phoneList.equals("")) {
    				if(list.get(i-1).getPhone()>0) {
        				phoneList=list.get(i-1).getPhone().toString();
    				}
    			}else {
    				if(list.get(i-1).getPhone()>0) {
        				log.info(phoneList+"-"+list.get(i-1).getPhone());
        				phoneList=phoneList+","+list.get(i-1).getPhone().toString();
    				}
    			}
    			if(i%900==0 || (t==time && i==list.size()))  {

    				String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
    				SmsSendRequest smsSingleRequest = new SmsSendRequest(form.getContent(), phoneList);
    				String requestJson = JSON.toJSONString(smsSingleRequest);

    				log.info("before request string is: " + requestJson);
    				String response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
    				if (response == null) {
    					response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
    				}
    				log.info("response after request result is :" + response);
    				if (response != null) {
    					SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
    					log.info("response  toString is :" + smsSingleResponse);
    				} else {
    					return ResultUtil.error(0,"失败发送",null);
    				}
    				t=t+1;
    				phoneList="";
    			}
    		}
        }
        returnDataUtil.setReturnObject(list.size());
		log.info("<CustomerOrderController>--<sendAllMessage>--end");
    	return returnDataUtil;
    }
    
    /**
     * 给部分用户发送短信
     */
    @ApiOperation(value = "给部分用户发送短信", notes = "给部分用户发送短信", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/sendMessage")
    @ResponseBody
    public ReturnDataUtil sendMessage(@RequestBody(required = false) UsersConsumptionRecordForm form,HttpServletRequest request) {
		log.info("<CustomerOrderController>--<sendMessage>--start");
		log.info("当前系统模式为"+active);
    	ReturnDataUtil returnDataUtil=new ReturnDataUtil();
        if(!active.equals("dev")) {
        	if(form.getPhoneList()!=null) {
    			if(form.getContent()!=null) {
    				log.info("<CustomerOrderController--sendMessage--start>");
    				// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
    				String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
    				// 手机号码
    				SmsSendRequest smsSingleRequest = new SmsSendRequest(form.getContent(), StringUtils.strip(form.getPhoneList().toString(),"[]"));

    				String requestJson = JSON.toJSONString(smsSingleRequest);

    				log.info("before request string is: " + requestJson);

    				String response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
    				if (response == null) {
    					response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
    				}
    				log.info("response after request result is :" + response);

    				if (response != null) {
    					SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
    					log.info("response  toString is :" + smsSingleResponse);
       					returnDataUtil.setReturnObject(form.getPhoneList().size());
    					return returnDataUtil;
    				} else {
    					return ResultUtil.error(0,"失败发送",null);
    				}
    			}
        	}
        }
        returnDataUtil.setReturnObject(form.getPhoneList().size());
		log.info("<CustomerOrderController>--<sendMessage>--end");
		return returnDataUtil;
    }
    
    
    
}
