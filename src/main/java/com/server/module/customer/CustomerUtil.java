package com.server.module.customer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.server.jwt.TokenAuthenticationService;
import com.server.module.sys.model.User;

public class CustomerUtil {

	public static Long getCustomerId(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String token = request.getHeader("token");
		if(token==null){
			token=request.getParameter("token");
		}
		if(StringUtils.isNotBlank(token)) {
			String param=TokenAuthenticationService.getClaimsFromToken(token).getSubject();
			String[] params = param.split(",");
			Long customerId = Long.valueOf(params[0]);
			return customerId;
		}
		return null;
	}
	
	public static User getUser(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String token = request.getHeader("token");
		if(token==null){
			token=request.getParameter("token");
		}
		try{
			String param=TokenAuthenticationService.getClaimsFromToken(token).getSubject();
			String[] params = param.split(",");
			Long customerId = Long.valueOf(params[0]);
			Integer companyId=Integer.valueOf(params[1]);
			String openId=String.valueOf(params[2]);
			User user=new User();
			user.setId(customerId);
			user.setCompanyId(companyId);
			user.setOpenId(openId);
			return user;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
