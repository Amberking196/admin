package com.server.module.system.game.send;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.server.module.system.game.GameDto;
import com.server.module.system.game.GamePrizeBean;
import com.server.util.ReturnDataUtil;


@Component
public class SendNoRewardProcessor implements SendPrizeProcessor {

	private static Logger log = LogManager.getLogger(SendNoRewardProcessor.class);

	@Override
	public ReturnDataUtil send(GamePrizeBean prize, GameDto gameDto) {
		log.info("<SendNoRewardProcessor>-----<send>-----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		returnDataUtil.setMessage("谢谢惠顾");
		returnDataUtil.setReturnObject(gameDto);
		log.info("<SendNoRewardProcessor>-----<send>-----end");
		return returnDataUtil;
	}
}
