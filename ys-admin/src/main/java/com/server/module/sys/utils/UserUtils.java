package com.server.module.sys.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.server.jwt.JwtTokenUtil;
import com.server.jwt.TokenAuthenticationService;
import com.server.module.sys.model.User;

import io.jsonwebtoken.Claims;

@Component
public class UserUtils {

	private static Logger log=LogManager.getLogger(UserUtils.class);

	@Autowired
	private  JwtTokenUtil jwtTokenUtil;
	
	/**
	 * 获取当前用户
	 * 
	 * @return
	 */
	public static User getUser() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String token = request.getHeader("token");
		if(token==null){
			token=request.getParameter("token");
		}
		log.info("token+++++++++++++++++++++"+token);
		try {
			String param=TokenAuthenticationService.getClaimsFromToken(token).getSubject();
			String[] params = param.split(",");
			Long userId = Long.valueOf(params[0]);
			Integer companyId = Integer.valueOf(params[1]);
			User user = new User();
			user.setId(userId);
			user.setCompanyId(companyId);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前发表文章的用户
	 *
	 * @return
	 */
	public static User getOffUser() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String offToken = request.getHeader("offToken");
		if(offToken==null){
			offToken=request.getParameter("offToken");
		}
		log.info("offToken+++++++++++++++++++++"+offToken);
		try {
			String param=TokenAuthenticationService.getClaimsFromToken(offToken).getSubject();
			String[] params = param.split(",");
			Long userId = Long.valueOf(params[0]);
			Integer companyId = Integer.valueOf(params[1]);
			User user = new User();
			user.setId(userId);
			user.setCompanyId(companyId);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 机器token  获取用户信息
	 * @author why
	 * @date 2019年3月16日 上午9:43:01 
	 * @return
	 */
	public  User getSmsUser() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String ysToken = request.getHeader("ysToken");
		if(ysToken==null){
			ysToken=request.getParameter("ysToken");
		}
		log.info("ysToken+++++++++++++++++++++"+ysToken);
		if (StringUtils.isNotBlank(ysToken) && ysToken.startsWith("Bearer ")) {
			String authToken = null;
			if (ysToken != null && ysToken.startsWith("Bearer ")) {
				try {
					authToken = ysToken.substring(7);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				log.info("authToken==" + authToken);
				if (authToken != null) {
					if (authToken.equals("undefined")) {// 经常有undefined 过来
						log.info("token is undefined =====");
						return null;
					}
					// 验证token是否过期,包含了验证jwt是否正确
					try {
						boolean flag = jwtTokenUtil.isTokenExpired(authToken);
						if (flag) {
							log.info("已过期===flag=" + flag);
							return null;
						} else {// 把用户id放到当前线程map
							log.info("获取token数据=");
							Claims claims = jwtTokenUtil.getClaimFromToken(authToken);
							if (claims != null) {
								String temp = claims.getSubject();
								log.info("sub===" + temp);
								User vo = JSON.parseObject(temp, User.class);
								return vo;
							} else {
								log.info("解析token为null");
								return null;
							}
						}
					} catch (Exception e) {
						// 有异常就是token解析失败
						e.printStackTrace();
						return null;
					}
				} else {
					log.info("传的token异常====" + authToken);
					return null;
				}
			}
		}
		return null;
	}

	

}
