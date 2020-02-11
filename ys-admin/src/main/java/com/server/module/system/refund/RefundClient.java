package com.server.module.system.refund;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lly835.bestpay.utils.JsonUtil;
import com.mysql.jdbc.log.Log;
import com.server.util.HttpUtil;
import com.server.util.JsonUtils;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
                
import org.apache.logging.log4j.LogManager;    import org.apache.logging.log4j.Logger;          
@Component    
public class RefundClient {
	
	@Value("${sms.refund.wxurl}")
	private String wxRefundUrl;
	@Value("${sms.refund.aliurl}")
	private String aliRefundUrl;
	@Value("${sms.refund.wxCustomerRefundUrl}")
	private String wxCustomerRefundUrl;
	private static Logger log = LogManager.getLogger(RefundController.class);
		ReturnDataUtil result = null;
		public ReturnDataUtil wxRefund(RefundDto refundDto){
		String refundJson = JsonUtil.toJson(refundDto);
		String jsonResponse = HttpUtil.post(wxRefundUrl,refundJson);
		log.info("jsonResponse:"+jsonResponse);
		if(StringUtil.isNotBlank(jsonResponse)){
			result = JsonUtils.toObject(jsonResponse, new TypeReference<ReturnDataUtil>(){});
		}else{
			result = ResultUtil.error();
		}
		return result;
	}
	
	public ReturnDataUtil aliRefund(RefundDto refundDto){
		ReturnDataUtil result = null;
		String refundJson = JsonUtil.toJson(refundDto);
		String jsonResponse = HttpUtil.post(aliRefundUrl,refundJson);
		if(StringUtil.isNotBlank(jsonResponse)){
			result = JsonUtils.toObject(jsonResponse, new TypeReference<ReturnDataUtil>(){});
		}else{
			result = ResultUtil.error();
		}
		return result;
	}
	
	public ReturnDataUtil wxCustomerRefund(RefundDto refundDto){
		ReturnDataUtil result = null;
		String refundJson = JsonUtil.toJson(refundDto);
		String jsonResponse = HttpUtil.post(wxCustomerRefundUrl,refundJson);
		if(StringUtil.isNotBlank(jsonResponse)){
			result = JsonUtils.toObject(jsonResponse, new TypeReference<ReturnDataUtil>(){});
		}else{
			result = ResultUtil.error();
		}
		return result;
	}
}
