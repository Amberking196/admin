package com.server.module.system.purchase.purchaseApply;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.server.common.utils.excel.ExportExcel;
import com.server.module.system.purchase.purchaseBill.PurchaseBillBean;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemBean;
import com.server.module.system.warehouseManage.supplierManage.SupplierBean;
import com.server.module.system.warehouseManage.supplierManage.SupplierService;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoBean;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoService;
import com.server.util.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.messageManagement.MessageManagementController;
import com.server.module.system.synthesizeManage.roleManage.RoleBean;
import com.server.module.system.synthesizeManage.roleManage.RoleManageService;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: yjr create time: 2018-08-31 17:27:57
 */
@Api(value = "PurchaseApplyBillController", description = "采购申请单")
@RestController
@RequestMapping("/purchaseApplyBill")
public class PurchaseApplyBillController {
	public static Logger log = LogManager.getLogger(PurchaseApplyBillController.class);
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private PurchaseApplyBillService purchaseApplyBillServiceImpl;
	@Autowired
	private RoleManageService roleManageService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private WarehouseInfoService warehouseInfoService;

	@ApiOperation(value = "采购申请单列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false)PurchaseApplyBillCondition condition,HttpServletRequest request) {
		log.info("<PurchaseApplyBillController>------<addPurchaseApply>----start");
		if(condition==null) {
			condition=new PurchaseApplyBillCondition();
		}
		//判断用户的角色是否为“采购专员”
		ReturnDataUtil data=new ReturnDataUtil();
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean loginUser = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if (loginUser == null) {// 直接返回，登入信息异常
			data.setMessage("登入信息异常");
			data.setStatus(0);
			return data;
		}
		 data = purchaseApplyBillServiceImpl.listPage(condition);
		 String role = loginUser.getRole();
		 //对字符串进行分割
		 String[] roles = role.split(",");
		 //根据roleId查询角色名
		 //用一容器接受角色名
		 Set<String> set= new HashSet<String>();
		 //暂时不用进行进行角色控制
		for (String id : roles) {
			RoleBean bean=roleManageService.findRoleById(Integer.parseInt(id));
			set.add(bean.getName());
		}
		//进行判断
		if(set.contains("采购专员")) {//设置可以操作采购申请单,暂时取消这个功能
			data.setStatus(1);
			data.setReJson("1");
		}else {
			data.setStatus(1);
			data.setReJson("1");
		}
		log.info("<PurchaseApplyBillController>------<addPurchaseApply>----end");
		return data;
	}

	@ApiOperation(value = "采购申请单添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody PurchaseApplyBillBean entity) {
		return new ReturnDataUtil(purchaseApplyBillServiceImpl.add(entity));
	}

	@ApiOperation(value = "采购申请单修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody PurchaseApplyBillBean entity) {
		return new ReturnDataUtil(purchaseApplyBillServiceImpl.update(entity));
	}

	@ApiOperation(value = "采购申请单删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		return new ReturnDataUtil(purchaseApplyBillServiceImpl.del(id));
	}

	@ApiOperation(value = "采购申请单的生成", notes = "addPurchaseApply", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/addPurchaseApply", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil addPurchaseApply(@RequestBody(required = false) PurchaseApplyAndItemBean bean,
			HttpServletRequest request) {
		log.info("<PurchaseApplyBillController>------<addPurchaseApply>----start");
		if(bean==null) {
			bean=new PurchaseApplyAndItemBean();
		}
		// 获取申请采购单申请人的信息
		ReturnDataUtil data = new ReturnDataUtil();
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean loginUser = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if (loginUser == null) {// 直接返回，登入信息异常
			data.setMessage("登入信息异常");
			data.setStatus(0);
			return data;
		}
		// 把登入的信息保存在bean中
		bean.setOperator(userId);
		bean.setOperatorName(loginUser.getName());
		// 保存数据
		data = purchaseApplyBillServiceImpl.addPurchaseApply(bean);
		if (data.getStatus() == 0) {
			// 保存成功
			data.setStatus(1);
			data.setMessage("申请单生成成功");
		} else {// 保存失败
			data.setStatus(0);
			data.setMessage("生成失败");
		}
		log.info("<PurchaseApplyBillController>------<addPurchaseApply>----end");
		return data;
	}
	@ApiOperation(value = "采购申请单提交", notes = "submitPurchaseApply", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/submitPurchaseApply", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil submitPurchaseApply(Integer id) {
		log.info("<PurchaseApplyBillController>------<submitPurchaseApply>----start");
		ReturnDataUtil data=new ReturnDataUtil();
		if(id==null) {//数据校验
			data.setMessage("参数异常");
			data.setStatus(0);
			return data;
		}
		data=purchaseApplyBillServiceImpl.submitPurchaseApply(id);
		if(data.getStatus()==0) {
			//操作成功
			data.setStatus(1);
			data.setMessage("提交成功");
		}else {
			data.setStatus(0);
			data.setMessage("提交失败");
		}
		log.info("<PurchaseApplyBillController>------<submitPurchaseApply>----end");
		return data;
	}
	@ApiOperation(value = "采购申请单删除", notes = "delete", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil delete(Integer id) {
		log.info("<PurchaseApplyBillController>------<delete>----start");
		ReturnDataUtil data=new ReturnDataUtil();
		if(id==null) {//数据校验
			data.setMessage("参数异常");
			data.setStatus(0);
			return data;
		}
		data=purchaseApplyBillServiceImpl.del(id);
		if(data.getStatus()==0) {
			//操作成功
			data.setStatus(1);
			data.setMessage("删除成功");
		}else {
			data.setStatus(0);
			data.setMessage("删除失败");
		}
		log.info("<PurchaseApplyBillController>------<delete>----end");
		return data;
	}
	@ApiOperation(value = "采购申请单详情", notes = "getPurchaseBillList", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getPurchaseBillList", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getPurchaseBillList(Integer id) {
		log.info("<PurchaseApplyBillController>------<getPurchaseBillList>----start");
		ReturnDataUtil  data=purchaseApplyBillServiceImpl.getPurchaseBillList(id);
		if(data.getStatus()==0) {
			//查询成功
			data.setStatus(1);
			data.setMessage("查询成功");
		}else {
			data.setStatus(0);
			data.setMessage("查询失败");
		}
		log.info("<PurchaseApplyBillController>------<getPurchaseBillList>----end");
		return data;
	}

	@ApiOperation(value = "导出采购单", notes = "exportBill", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/exportBill", produces = "application/json;charset=UTF-8")
	public void exportBill(Integer id, String endTime,HttpServletRequest request, HttpServletResponse response) {
		ReturnDataUtil  data=purchaseApplyBillServiceImpl.getPurchaseBillList(id);
		List<PurchaseBillBean> listBill=(List<PurchaseBillBean>)data.getReturnObject();
		PurchaseBillBean bill=listBill.get(0);
		List<PurchaseBillItemBean> list=bill.getList();
		PurchaseBillItemBean heji=new PurchaseBillItemBean();
		double totalMoney=0;
		for (PurchaseBillItemBean billObj : list) {
			billObj.setTotalMoney(billObj.getPrice()*billObj.getQuantity());
			totalMoney=totalMoney+billObj.getTotalMoney();
		}
		heji.setItemName("合计");
		heji.setTotalMoney(totalMoney);
		list.add(heji);
		PurchaseBillItemBean item=list.get(0);
		SupplierBean supplierBean=supplierService.get(item.getSupplierId());
		WarehouseInfoBean warehouseInfoBean=warehouseInfoService.get(bill.getWarehouseId());
		AdminUserBean adminUserBean=(AdminUserBean) adminUserService.findUserById(warehouseInfoBean.getPrincipal().longValue()).getReturnObject();
		List<String> summaryList= Lists.newArrayList();
		List<String> rightSummaryList= Lists.newArrayList();
		summaryList.add("订单编号:"+bill.getNumber());
		summaryList.add("         ");
		summaryList.add("甲方：广州优水到家工程网络技术有限公司");
		summaryList.add("     公司地址：广州市黄埔区开源大道11号B10栋第4层");
		summaryList.add("     联系人及电话：蒋艾明  13922717628");
		summaryList.add("         ");
		summaryList.add("乙方:"+supplierBean.getCompanyName());
		summaryList.add("     公司地址:"+supplierBean.getAddress());
		summaryList.add("     联系人及电话:"+supplierBean.getName()+"  "+supplierBean.getPhone());
		summaryList.add("          ");
		summaryList.add("     交货日期:"+endTime+" 前");
		summaryList.add("     交货地点:"+warehouseInfoBean.getLocation());
		summaryList.add("收货人及电话:"+adminUserBean.getName()+" "+adminUserBean.getPhone());
		summaryList.add("           ");
		summaryList.add("甲方订单确认人:(签名）           乙方订单确认人：     ");

		summaryList.add("            ");
		summaryList.add("根据甲乙方签订的《商品购销合同》，乙方订购产品的规格、数量如下：");
		//交货日期:2018年7月5日前
		String fileName = "采购单" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";

		try {
			new ExportExcel("采购单", summaryList, rightSummaryList, PurchaseBillItemBean.class).setDataList(list)
					.write(response, fileName).dispose();
		}catch (Exception e){
			e.printStackTrace();
		}


	}
}
