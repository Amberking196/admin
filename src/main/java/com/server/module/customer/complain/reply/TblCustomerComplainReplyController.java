package com.server.module.customer.complain.reply;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.customer.CustomerUtil;
import com.server.module.customer.complain.TblCustomerComplainBean;
import com.server.module.customer.complain.TblCustomerComplainService;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserDao;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: yjr create time: 2018-12-04 11:27:38
 */
@Api(value = "TblCustomerComplainReplyController", description = "故障申诉回复")
@RestController
@RequestMapping("/complainReply")
public class TblCustomerComplainReplyController {

	private static Logger log = LogManager.getLogger(TblCustomerComplainReplyController.class);
	@Autowired
	private TblCustomerComplainReplyService tblCustomerComplainReplyServiceImpl;
	@Autowired
	private AdminUserDao adminUserDao;
	@Autowired
	private TblCustomerComplainService customerComplainService;
	@Autowired
	private UserUtils userUtils;

	@ApiOperation(value = "故障申诉回复列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(TblCustomerComplainReplyCondition condition) {
		return tblCustomerComplainReplyServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "故障申诉回复添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/reply", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil reply(@RequestBody TblCustomerComplainReplyBean entity) {
		entity.setCreateTime(new Date());
		User user = UserUtils.getUser();
		entity.setCreateUser(user.getId());
		AdminUserBean adminUserBean = adminUserDao.findUserById(user.getId());
		entity.setCreateName(adminUserBean.getName());
		entity.setSrc(1);// 客户回复
		TblCustomerComplainBean complainBean = customerComplainService.get(entity.getComplainId());
		complainBean.setState(1);// 已回复
		customerComplainService.update(complainBean);
		return new ReturnDataUtil(tblCustomerComplainReplyServiceImpl.add(entity));
	}

	@ApiOperation(value = "故障申诉回复列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getComplain", produces = "application/json;charset=UTF-8")
	public TblCustomerComplainBean getComplain(Integer id) {

		return tblCustomerComplainReplyServiceImpl.getComplain(id);
	}

	@ApiOperation(value = "客户回复列表", notes = "listAllCustomerReply", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listAllCustomerReply", produces = "application/json;charset=UTF-8")
	public List<TblCustomerComplainReplyBean> listAllCustomerReply(Integer complainId) {
		return tblCustomerComplainReplyServiceImpl.listAllCustomerReply(complainId);
	}

	@ApiOperation(value = "用户故障申报回复次数判断", notes = "isReply", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/isReply", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil isReply(Long complainId) {
		log.info("<TblCustomerComplainReplyController>------<isReply>-----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		List<TblCustomerComplainReplyBean> list = tblCustomerComplainReplyServiceImpl.listReplyBean(complainId);
		if (list != null && list.size() > 0) {
			int count = 0;
			for (TblCustomerComplainReplyBean bean : list) {
				if (bean.getSrc() == 0) {
					count++;
				}
			}
			if (count >= 3) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("亲，提问已经三次了，不可以继续提问了哟！亲，如还有问题，请电话联系客服，谢谢！");
			} else {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("可以继续提问！");
			}
		} else {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("可以继续提问！");
		}
		log.info("<TblCustomerComplainReplyController>------<isReply>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "用户故障申报提问", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody TblCustomerComplainReplyBean entity) {
		log.info("<TblCustomerComplainReplyController>------<add>-----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		Long customerId = CustomerUtil.getCustomerId();
		if (customerId == null) {
			customerId = userUtils.getSmsUser().getId();
		}
		entity.setSrc(0);
		entity.setCreateUser(customerId);
		TblCustomerComplainReplyBean bean = tblCustomerComplainReplyServiceImpl.add(entity);
		if (bean != null) {
			TblCustomerComplainBean complainBean = customerComplainService.get(entity.getComplainId());
			complainBean.setState(0);// 未已回复
			// 更新状态
			boolean update = customerComplainService.update(complainBean);
			if (update) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("提问成功！");
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("提问失败！");
			}
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("提问失败！");
		}
		log.info("<TblCustomerComplainReplyController>------<add>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "定时器使用查询接口", notes = "replyDetails", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/replyDetails", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil replyDetails(Long[] complainIds) {
		log.info("<TblCustomerComplainReplyController>------<replyDetails>-----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		List<List<TblCustomerComplainReplyBean>> listAll = new ArrayList<List<TblCustomerComplainReplyBean>>();
		// 进行排序
		Arrays.sort(complainIds);
		for (int i = complainIds.length - 1; i >= 0; i--) {
			log.info("============complainId===========" + complainIds[i]);
			List<TblCustomerComplainReplyBean> list = tblCustomerComplainReplyServiceImpl.listReplyBean(complainIds[i]);
			listAll.add(list);
		}
		if (listAll != null && listAll.size() > 0) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setReturnObject(listAll);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setReturnObject(listAll);
		}
		log.info("<TblCustomerComplainReplyController>------<replyDetails>-----end");
		return returnDataUtil;
	}
}
