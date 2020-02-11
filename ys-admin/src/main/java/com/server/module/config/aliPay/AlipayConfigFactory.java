package com.server.module.config.aliPay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.config.aliPay.aliInfo.AliInfoService;
import com.server.module.config.aliPay.aliInfo.AliMerchantInfoBean;
import com.server.module.config.aliPay.aliInfo.AliPayConfigBean;
import com.server.redis.RedisClient;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;


@Component
public class AlipayConfigFactory {

	@Autowired
	private AliPayConfig alipayConfig;
	
	@Autowired
	private AliInfoService aliInfoService;
	
	@Autowired
	private RedisClient redisService;
	
	private static final Map<Integer,AliPayConfig> map = new HashMap<Integer,AliPayConfig>();
	private Map<Integer,String> redisFlagMap = new HashMap<Integer,String>();
	private final String redisName = "AlipayConfig";
	/**
	 * 初始化
	 * @author hebiting
	 * @date 2018年7月7日上午11:38:52
	 */
	@PostConstruct
	public void initMethod(){
		List<AliMerchantInfoBean> allAliMerchantInfo = aliInfoService.getAllAliMerchantInfo();
		allAliMerchantInfo.forEach(merchantInfo -> {
			AliPayConfig clone = null;
			try {
				clone = (AliPayConfig)alipayConfig.clone();
				clone.setSeller_id(merchantInfo.getPartner());
				clone.setPartner(merchantInfo.getPartner());
				clone.setKey(merchantInfo.getKey());
				clone.setTemplate_id(merchantInfo.getTemplateId());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			redisFlagMap.put(merchantInfo.getCompanyId(), "1");
			map.put(merchantInfo.getCompanyId(), clone);
		});
	}
	
	public AliPayConfig getAlipayConfig(Integer companyId){
		ensureConfig(companyId);
		AliPayConfig aliConfig = map.get(companyId);
		if(aliConfig == null){
			aliConfig = map.get(CompanyEnum.YOUSHUIDAOJIA.getIndex());
		}
		return aliConfig;
	}
	
	private synchronized void ensureConfig(Integer companyId){
		boolean needUpdate = false;
		String flag = redisFlagMap.get(companyId);
		String redisFlag = redisService.get(redisName+companyId);
		boolean flagBlank = StringUtil.isBlank(flag);
		boolean redisBlank = StringUtil.isBlank(redisFlag);
		if (!flagBlank && redisBlank) {
			redisService.set(redisName+companyId,flag);
		} else if (!flagBlank && !redisBlank) {
			if(!flag.equals(redisFlag)){
				needUpdate = true;
				redisFlagMap.put(companyId, redisFlag);
			}
		} else {
			needUpdate = true;
			redisFlagMap.put(companyId, "1");
			redisService.set(redisName+companyId,"1");
		}
		if(needUpdate){
			AliMerchantInfoBean merchantInfo = aliInfoService.getAliMerchantInfo(companyId);
			if(merchantInfo!=null){
				AliPayConfig clone = null;
				try {
					clone = (AliPayConfig)alipayConfig.clone();
					clone.setSeller_id(merchantInfo.getPartner());
					clone.setPartner(merchantInfo.getPartner());
					clone.setKey(merchantInfo.getKey());
					clone.setTemplate_id(merchantInfo.getTemplateId());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				map.put(merchantInfo.getCompanyId(), clone);
			}
		}
	}
	
	public synchronized boolean updateConfig(AliPayConfigBean alipayBean){
		AliPayConfig clone = null;
		try {
			clone = (AliPayConfig)alipayConfig.clone();
			clone.setSeller_id(alipayBean.getPartner());
			clone.setPartner(alipayBean.getPartner());
			clone.setKey(alipayBean.getKey());
			clone.setTemplate_id(alipayBean.getTemplate_id());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return false;
		}
		map.put(alipayBean.getCompanyId(), clone);
		return true;
	}
}
