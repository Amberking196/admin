package com.server.module.config.gzh;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.redis.RedisClient;
@Component
public class WxMsgDetection {
	@Autowired
	private WxTicketService wxTicketService;
	@Autowired
	private RedisClient redisClient;

	public boolean detectionAccessToken(Map<String,Object> msg){
		boolean result = true;
		if(msg.containsKey("errcode")){
			String errcode = msg.get("errcode").toString();
			if(("40001").equals(errcode)){
				result = false;
				redisClient.del(wxTicketService.redisAccessToken);
			}else if(("0").equals(errcode)){
				result = true;
			}else{
				result = false;
			}
		}
		return result;
	}
}
