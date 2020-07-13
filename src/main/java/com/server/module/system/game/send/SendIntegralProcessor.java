package com.server.module.system.game.send;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.game.GameDto;
import com.server.module.system.game.GamePrizeBean;
import com.server.module.system.userManage.CustomerService;
import com.server.util.ReturnDataUtil;

/**
 * 积分发送
 * 
 * @author why
 * @date: 2019年3月7日 上午10:21:19
 */
@Component
public class SendIntegralProcessor implements SendPrizeProcessor {

	private static Logger log = LogManager.getLogger(SendCarryWaterProcessor.class);
	
	@Autowired
	private CustomerService tblCustomerServiceImpl;
	@Autowired
	private UserUtils userUtils;
	
	@Override
	public ReturnDataUtil send(GamePrizeBean prize, GameDto gameDto) {
		log.info("<SendIntegralProcessor>-----<send>-----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		tblCustomerServiceImpl.updateCustomerBean(userUtils.getSmsUser().getId(), prize.getAmount().longValue(), 1);
		returnDataUtil.setMessage("恭喜您抽中" + prize.getAmount() + "积分，请前往我的信息里面查看积分！");
		returnDataUtil.setReturnObject(gameDto);
		log.info("<SendIntegralProcessor>-----<send>-----end");
		return returnDataUtil;
	}

}
