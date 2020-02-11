package com.server.module.config.pay;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.server.github.wxpay.sdk.WXPay;
import com.server.github.wxpay.sdk.WXPayConfig;
import com.server.module.system.companyManage.wxPayConfig.MerchantInfo;
import com.server.module.system.companyManage.wxPayConfig.MerchantInfoDao;
import com.server.redis.RedisClient;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;
import com.server.util.stateEnum.MerchantTypeEnum;

@Component
@Scope("singleton")
public class WxPayConfigFactory {
	
	@Autowired
	private MerchantInfoDao merchantInfoDao;
	@Autowired
	private RedisClient  redisClient;
	
	private Map<Integer,String> redisFlagMap = new HashMap<Integer,String>();

	private final Map<Integer,WXPayConfig> wxPayConfigMap = new HashMap<Integer,WXPayConfig>();
	
	private final Map<Integer,WXPay> wxPayMap = new HashMap<Integer,WXPay>();
	
	private final String redisName = "WxPayConfig";
	
	/**
	 * 初始化
	 * @author hebiting
	 * @date 2018年7月7日上午11:38:43
	 */
	@PostConstruct
	public void initMethod(){
		List<MerchantInfo> merchantInfoList = merchantInfoDao.findMerchantInfo(MerchantTypeEnum.MERCHANT.getType());
		for (MerchantInfo merchantInfo : merchantInfoList) {
			MyWXPayConfig wxPayConfig = new MyWXPayConfig();
			wxPayConfig.setAppID(merchantInfo.getAppId());
			wxPayConfig.setMchID(merchantInfo.getMchId());
			wxPayConfig.setKey(merchantInfo.getMchKey());
			wxPayConfig.setPlanId(merchantInfo.getPlanId());
			wxPayConfig.setCompanyId(merchantInfo.getCompanyId());
			wxPayConfig.setUsingCompanyConfig(merchantInfo.getUsingCompanyConfig());
			wxPayConfig.setIWXPayDomain(WXPayDomainSimpleImpl.instance());
			wxPayConfig.setKeyPath(merchantInfo.getKeyPath());
			wxPayConfig.setAppAppID(merchantInfo.getAppAppId());
			wxPayConfig.setAppKey(merchantInfo.getAppKey());
			redisFlagMap.put(merchantInfo.getCompanyId(), "1");
			wxPayConfigMap.put(merchantInfo.getCompanyId(), wxPayConfig);
			WXPay wxPay = null;
			try {
				wxPay = new WXPay(wxPayConfig);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			wxPayMap.put(merchantInfo.getCompanyId(), wxPay);
		}
	}
	
	public WXPayConfig getWXPayConfig(Integer companyId){
		ensureConfig(companyId);
		WXPayConfig wxPayConfig = wxPayConfigMap.get(companyId);
		if(wxPayConfig == null){
			wxPayConfig = wxPayConfigMap.get(CompanyEnum.YOUSHUIDAOJIA.getIndex());
		}
		return wxPayConfig;
	}
	
	private synchronized void ensureConfig(Integer companyId){
		boolean needUpdate = false;
		String flag = redisFlagMap.get(companyId);
		String redisFlag = redisClient.get(redisName+companyId);
		boolean flagBlank = StringUtil.isBlank(flag);
		boolean redisBlank = StringUtil.isBlank(redisFlag);
		if (!flagBlank && redisBlank) {
			redisClient.set(redisName+companyId,flag);
		} else if (!flagBlank && !redisBlank) {
			if(!flag.equals(redisFlag)){
				needUpdate = true;
				redisFlagMap.put(companyId, redisFlag);
			}
		} else {
			needUpdate = true;
			redisFlagMap.put(companyId, "1");
			redisClient.set(redisName+companyId,"1");
		}
		if(needUpdate){
			MerchantInfo merchant = merchantInfoDao.findMerchantInfoByCompanyId(companyId);
			if(merchant!=null){
				MyWXPayConfig wxPayConfig = new MyWXPayConfig();
				wxPayConfig.setAppID(merchant.getAppId());
				wxPayConfig.setMchID(merchant.getMchId());
				wxPayConfig.setKey(merchant.getMchKey());
				wxPayConfig.setPlanId(merchant.getPlanId());
				wxPayConfig.setCompanyId(merchant.getCompanyId());
				wxPayConfig.setUsingCompanyConfig(merchant.getUsingCompanyConfig());
				wxPayConfig.setIWXPayDomain(WXPayDomainSimpleImpl.instance());
				wxPayConfig.setKeyPath(merchant.getKeyPath());
				wxPayConfigMap.put(merchant.getCompanyId(), wxPayConfig);
				WXPay wxPay = null;
				try {
					wxPay = new WXPay(wxPayConfig);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				wxPayMap.put(merchant.getCompanyId(), wxPay);
			}
		}
	}
	
	
	public WXPayConfig getWXPayConfig(final String mchId){
		Collection<WXPayConfig> values = wxPayConfigMap.values();
		for (WXPayConfig wxPayConfig : values) {
			if(wxPayConfig.getMchID().equals(mchId)){
				return wxPayConfig;
			}
		}
		return getWXPayConfig(CompanyEnum.YOUSHUIDAOJIA.getIndex());
	}
	
	public WXPay getWXPay(Integer companyId){
		ensureConfig(companyId);
		WXPay wxPay = wxPayMap.get(companyId);
		if(wxPay == null){
			wxPay = wxPayMap.get(CompanyEnum.YOUSHUIDAOJIA.getIndex());
		}
		return wxPay;
	}

	public Map<Integer, WXPayConfig> getWxPayConfigMap() {
		return wxPayConfigMap;
	}

	public Map<Integer, WXPay> getWxPayMap() {
		return wxPayMap;
	}
	
	public synchronized void updateConfig(MerchantInfo merchantInfo){
		MyWXPayConfig wxPayConfig = new MyWXPayConfig();
		wxPayConfig.setAppID(merchantInfo.getAppId());
		wxPayConfig.setMchID(merchantInfo.getMchId());
		wxPayConfig.setKey(merchantInfo.getMchKey());
		wxPayConfig.setPlanId(merchantInfo.getPlanId());
		wxPayConfig.setCompanyId(merchantInfo.getCompanyId());
		wxPayConfig.setUsingCompanyConfig(merchantInfo.getUsingCompanyConfig());
		wxPayConfig.setIWXPayDomain(WXPayDomainSimpleImpl.instance());
		wxPayConfig.setKeyPath(merchantInfo.getKeyPath());
		wxPayConfigMap.put(merchantInfo.getCompanyId(), wxPayConfig);
		WXPay wxPay = null;
		try {
			wxPay = new WXPay(wxPayConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
		wxPayMap.put(merchantInfo.getCompanyId(), wxPay);
	}
}
