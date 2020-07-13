package com.server.module.system.messageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.machineManage.machineList.VendingMachinesInfoCondition;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "MessageManagementController", description = "留言管理")
@RestController
@RequestMapping("/messageManagement")
public class MessageManagementController {
	public static Logger log = LogManager.getLogger(MessageManagementController.class);
	
	@Autowired
	private MessageManagementService MessageManagementServiceImpl;
	@ApiOperation(value = "留言管理售货机列表", notes = "留言管理售货机列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) VendingMachinesInfoCondition condition) {
		log.info("<MessageManagementController>------<listPage>----start");
		if(condition==null) {
			condition=new VendingMachinesInfoCondition();
		}
		log.info("<MessageManagementController>------<listPage>----end");
		return MessageManagementServiceImpl.listPage(condition);
	}
	@ApiOperation(value = "留言管理留言列表", notes = "留言管理留言列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/messageListPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil messageListPage(@RequestBody(required=false) MessageManagementForm form) {
		log.info("<MessageManagementController>------<messageListPage>----start");
		
		if(form==null) {
			form=new MessageManagementForm();
		}
		ReturnDataUtil data=MessageManagementServiceImpl.messageListPage(form);
		log.info("<MessageManagementController>------<messageListPage>----end");
		return data;
		
	}
	@ApiOperation(value = "评论列表", notes = "评论列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/messageCommentListPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil messageCommentListPage(@RequestBody(required=false) MessageCommentForm form) {
		log.info("<MessageManagementController>------<messageCommentListPage>----start");
		if(form==null) {
			form=new MessageCommentForm();
		}
		ReturnDataUtil data=MessageManagementServiceImpl.messageCommentListPage(form);
		log.info("<MessageManagementController>------<messageCommentListPage>----end");
		return data;
	}
}
