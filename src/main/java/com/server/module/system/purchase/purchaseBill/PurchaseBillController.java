package com.server.module.system.purchase.purchaseBill;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.server.module.system.purchase.purchaseApply.PurchaseApplyAndItemBean;
import com.server.module.system.purchase.purchaseApply.PurchaseApplyBillBean;
import com.server.module.system.purchase.purchaseApply.PurchaseApplyBillService;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemBean;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemService;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemServiceImpl;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: yjr create time: 2018-09-03 16:23:53
 */
@Api(value = "PurchaseBillController", description = "采购单")
@RestController
@RequestMapping("/purchaseBill")
public class PurchaseBillController {
	public static Logger log = LogManager.getLogger(PurchaseBillController.class);
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private PurchaseBillService purchaseBillServiceImpl;
	@Autowired
	private PurchaseApplyBillService purchaseApplyBillServiceImpl;
	@Autowired
	private ReturnDataUtil  returnDataUtil;
	@Autowired
	private PurchaseBillItemService purchaseBillItemServiceImpl;
	
	@ApiOperation(value = "采购单管理列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) PurchaseBillForm purchaseBillForm) {
		log.info("<PurchaseBillController>-----<listPage>-------start");
		if(purchaseBillForm==null) {
			purchaseBillForm=new PurchaseBillForm();
		}
		returnDataUtil=purchaseBillServiceImpl.listPage(purchaseBillForm);
		log.info("<PurchaseBillController>-----<listPage>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "采购单商品列表", notes = "getItemBean", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getItemBean", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getItemBean(String number) {
		log.info("<PurchaseBillController>-----<getItemBean>-------start");
		PurchaseBillBean itemBean = purchaseBillServiceImpl.getItemBean(number);
		if(itemBean!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("采购单详情查看成功！");
			returnDataUtil.setReturnObject(itemBean);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("采购单输入有误，请重新输入！");
			returnDataUtil.setReturnObject(itemBean);
		}
		log.info("<PurchaseBillController>-----<getItemBean>-------end");
		return returnDataUtil;
	}

 
	@ApiOperation(value = "采购申请单审核同过和不通过和生成采购单", notes = "auditing", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/auditing", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil auditing(HttpServletRequest request,
			@RequestBody(required = false) PurchaseApplyAndItemBean bean) {
		log.info("<PurchaseApplyBillController>------<auditing>----start");
		if (bean == null) {
			bean = new PurchaseApplyAndItemBean();
		}
		// 获取申请采购单审核人的信息
		ReturnDataUtil data = new ReturnDataUtil();
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean loginUser = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		// 设置审核人姓名和id
		if (loginUser == null) {// 登入错误
			data.setStatus(0);
			data.setMessage("登入异常");
			return data;
		}
		//查询采购申请单申请人
		PurchaseApplyBillBean beanById = purchaseApplyBillServiceImpl.getBeanById(bean.getId().intValue());
		bean.setNumber(beanById.getNumber());//设置单号
		bean.setOperator(beanById.getOperator());
		bean.setOperatorName(beanById.getOperatorName());
		bean.setAuditor(userId);
		bean.setAuditorName(loginUser.getName());
		// 判断是否为不通过
		if (bean.getType() == 0) {// 审核不通过
			data = purchaseBillServiceImpl.checkFalse(bean);
			if (data.getStatus() == 0) {
				// 保存成功
				data.setMessage("操作成功");
				data.setStatus(1);
			} else {
				data.setMessage("操作失败");
				data.setStatus(0);
			}
			log.info("<PurchaseApplyBillController>------<auditing>----end");
			return data;// 操作结束，返回结果
		} else if (bean.getType() == 1) {// 审核通过
			data = purchaseBillServiceImpl.auditing(bean);
			if (data.getStatus() == 0) {
				// 保存成功
				data.setMessage("操作成功");
				data.setStatus(1);
			} else {
				data.setMessage("操作失败");
				data.setStatus(0);
			}
			log.info("<PurchaseApplyBillController>------<auditing>----end");
			return data;

		}
		return null;
	}
	/**
	 * 删除采购单
	 *@author why
	 *@date 2018年9月8日-上午10:28:28
	 *@param number
	 *@return
	 */
	@ApiOperation(value = "删除采购单", notes = "delete", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil delete(String number) {
		log.info("<PurchaseBillController>-----<delete>-------start");
		PurchaseBillBean itemBean = purchaseBillServiceImpl.getItemBean(number);
		if(itemBean!=null) {
			//标记是否全部删除采购单的商品
			Integer flag=0;
			List<PurchaseBillItemBean> list = itemBean.getList();
			for (PurchaseBillItemBean purchaseBillItemBean : list) {
				boolean del = purchaseBillItemServiceImpl.del(purchaseBillItemBean.getId());
				if(del) {
					flag++;
				}
			}
			if(flag.equals(list.size())) {
				boolean del = purchaseBillServiceImpl.del(itemBean.getId());
				if(del) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("采购单删除成功！");
				}else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("采购单删除失败！");
				}
			}else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("采购单删除失败！");
			}
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("采购单删除失败！");
		}
		log.info("<PurchaseBillController>-----<delete>-------end");
		return returnDataUtil;
	}
}
