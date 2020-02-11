package com.server.module.system.userManage;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.server.module.config.gzh.MyWXGzhConfig;
import com.server.module.config.gzh.WXGzhConfigFactory;
import com.server.module.config.gzh.WxTicketService;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.CompanyEnum;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

	private static Logger log=LogManager.getLogger(CustomerServiceImpl.class);
	@Autowired
	CustomerDao customerDao;
	@Autowired
	private WXGzhConfigFactory gzhConfigFactory;
	@Autowired 
	private WxTicketService wxTicketService;
	
	
	@Override
	public CustomerBean findCustomerById(Long id) {
		return customerDao.findCustomerById(id);
	}
	
	
	@Override
	public ReturnDataUtil findCustomer(UserManagerForm userManagerForm) {
		
		return customerDao.findCustomer(userManagerForm);
	}


	@Override
	public ReturnDataUtil queryDetail(UserManagerForm userManagerForm) {
		return customerDao.queryDetail(userManagerForm);
	}
	

	public ReturnDataUtil queryInvite(UserManagerForm userManagerForm) {
		return customerDao.queryInvite(userManagerForm);
	}

    public ReturnDataUtil queryInviteDetail(UserManagerForm userManagerForm){
		return customerDao.queryInviteDetail(userManagerForm);
	}

    @Override
	public boolean updateCustomerBean(Long customerId, Long rewards, Integer type) {
		log.info("<TblCustomerServiceImpl>------<updateCustomerBean>------start");
		boolean flag = customerDao.updateCustomerBean(customerId, rewards, type);
		log.info("<TblCustomerServiceImpl>------<updateCustomerBean>------end");
		return flag;
	}
    
    @Override
	public Integer isFirstBuy(Long customerId) {
		log.info("<CustomerServiceImpl>-----<isFirstBuy>----start");
		Integer firstBuy = customerDao.isFirstBuy(customerId);
		log.info("<CustomerServiceImpl>-----<isFirstBuy>----end");
		return firstBuy;
	}
    
    @Override
	public Integer isStoreFirstBuy(Long customerId) {
		log.info("<CustomerServiceImpl>-----<isStoreFirstBuy>----start");
		Integer firstBuy = customerDao.isStoreFirstBuy(customerId);
		log.info("<CustomerServiceImpl>-----<isStoreFirstBuy>----end");
		return firstBuy;
	}
    
    @Override
	public void sendWechatMessage(String openId,String message) {
		log.info("<CustomerDaoImpl>----<sendWechatMessage>----start");
		MyWXGzhConfig myWXGzhConfig = gzhConfigFactory.getMyWXGzhConfig(CompanyEnum.YOUSHUIDAOJIA.getIndex());
		String accessToken=wxTicketService.getAccessToken(myWXGzhConfig);
        RestTemplate rest = new RestTemplate();
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken ;
        Map<String,Object> param = new HashMap<>();
        param.put("touser", openId);
        param.put("msgtype", "text");
        
        Map<String,Object> text = new HashMap<>();
        text.put("content", message);
        param.put("text", text);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HttpEntity requestEntity = new HttpEntity(param, headers);
        Map result = null;
        try {
            ResponseEntity<Map> entity = rest.exchange(url, HttpMethod.POST, requestEntity,Map.class, new Object[0]);
            log.info("调用发送客服信息接口返回结果:" + entity.getBody());
            result = (Map) entity.getBody();
        } catch (Exception e) {
            log.error("调用发送客服信息接口异常",e);
        }
		log.info("<CustomerDaoImpl>----<sendWechatMessage>----end");
	}
}
