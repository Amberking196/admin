package com.server.module.customer.product.spellGroupShare;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.module.customer.CustomerUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 * @author why
 * @date: 2019年1月16日 下午9:18:17
 */
@Api(value = "SpellGroupShareController", description = "团购分享相关信息")
@RestController
@RequestMapping("/spellGroupSharer")
public class SpellGroupShareController {

	private static Logger log=LogManager.getLogger(SpellGroupShareController.class);
	
	@Autowired
	private SpellGroupShareService groupShareServiceImpl;

	@ApiOperation(value = "分享后用户点击页面查询", notes = "分享后用户点击页面查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/list")
	public ReturnDataUtil list(Long customerSpellGroupId) {
		log.info("<SpellGroupShareController>----<list>-----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		SpellGroupShareBean bean = groupShareServiceImpl.list(customerSpellGroupId);
		if(bean!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setReturnObject(bean);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("亲，该团已经完成（结束）了，请参加其他拼团！");
		}
		log.info("<SpellGroupShareController>----<list>-----end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "用户支付完成后 查询信息", notes = "用户支付完成后 查询信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/payFinishList")
	public ReturnDataUtil payFinishList(Long orderId) {
		log.info("<SpellGroupShareController>----<payFinishList>-----start");
		SpellGroupShareBean bean = groupShareServiceImpl.payFinishList(orderId);
		log.info("<SpellGroupShareController>----<payFinishList>-----end");
		return new ReturnDataUtil(bean);
	}
}
