package com.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.server.module.system.officialManage.officialUser.OfficialConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.jwt.JwtTokenUtil;
import com.server.jwt.TokenAuthenticationService;
import com.server.module.sys.model.User;
import com.server.module.system.adminUser.AdminConstant;
import com.server.util.ReturnDataUtil;

import io.jsonwebtoken.Claims;

@Component
public class WebFilter implements Filter {

	public static Logger log = LogManager.getLogger(WebFilter.class);
	@Autowired
	private TokenAuthenticationService tokenService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		log.info("WebFilter---------doFilter------ start");
		boolean chektoken = false;
		String headerToken = ((HttpServletRequest) request).getHeader("token");
		String requestToken = request.getParameter("token");

		String headerToken1 = ((HttpServletRequest) request).getHeader("ysToken");
		String requestToken1 = request.getParameter("ysToken");

		String headerToken2 = ((HttpServletRequest) request).getHeader("offToken");
		String requestToken2 = request.getParameter("offToken");

		String token = null;
		String ysToken = null;
		String offToken = null;
		if (headerToken != null || headerToken1 != null || headerToken2 != null) {
			token = headerToken;
			ysToken = headerToken1;
			offToken = headerToken2;
			chektoken = true;
		}

		if (requestToken != null || requestToken1 != null || requestToken2 !=null) {
			token = requestToken;
			ysToken = requestToken1;
			offToken = requestToken2;
			chektoken = true;
		}

		//User相关的token
		if (StringUtils.isNotBlank(token)  && !token.equals("login") && !token.equals("webapp")) {
			tokenService = new TokenAuthenticationService();
			chektoken = tokenService.checkToken(token);
			if (chektoken) {
				String param = tokenService.getIdByToken(token);
				String[] params = param.split(",");
				Long userId = Long.valueOf(params[0]);
				Integer companyId = Integer.valueOf(params[1]);
				log.info("userId=================" + userId);
				request.setAttribute(AdminConstant.LOGIN_USER_ID, userId);
				request.setAttribute(AdminConstant.LOGIN_USER_COMPANYID, companyId);
				if (companyId == 0 && params.length == 2) {
					chektoken = false;
				}

			}
		}

		//offUser相关的token====用来发表文章的小系统
		if (StringUtils.isNotBlank(offToken) && !offToken.equals("register")) {
			tokenService = new TokenAuthenticationService();
			chektoken = tokenService.checkToken(offToken);
			if (chektoken) {
				//根据token拿到id
				String param = tokenService.getIdByToken(offToken);
				String[] params = param.split(",");
				Long userId = Long.valueOf(params[0]);
				Integer companyId = Integer.valueOf(params[1]);
				log.info("offuserId=================" + userId);
				request.setAttribute(OfficialConstant.LOGIN_OFF_ID, userId);
				request.setAttribute(OfficialConstant.LOGIN_OFF_COMPANYID, companyId);
				if (companyId == 0 && params.length == 2) {
					chektoken = false;
				}

			}
		}

		//机器相关的token
		if (StringUtils.isNotBlank(ysToken) && ysToken.startsWith("Bearer ")) {
			String authToken = null;
			if (ysToken != null && ysToken.startsWith("Bearer ")) {
				try {
					authToken = ysToken.substring(7);
				} catch (Exception e) {
					e.printStackTrace();
					chektoken = false;
				}
				log.info("authToken==" + authToken);
				if (authToken != null) {
					if (authToken.equals("undefined")) {// 经常有undefined 过来
						log.info("token is undefined =====");
						chektoken = false;
					}
					// 验证token是否过期,包含了验证jwt是否正确
					try {
						boolean flag = jwtTokenUtil.isTokenExpired(authToken);
						if (flag) {
							log.info("已过期===flag=" + flag);
							chektoken = false;
						} else {// 把用户id放到当前线程map
							log.info("获取token数据=");
							Claims claims = jwtTokenUtil.getClaimFromToken(authToken);
							if (claims != null) {
								String temp = claims.getSubject();
								log.info("sub===" + temp);
								User vo = JSON.parseObject(temp, User.class);
								request.setAttribute(AdminConstant.LOGIN_USER_ID, vo.getId());
								request.setAttribute(AdminConstant.LOGIN_USER_COMPANYID, vo.getCompanyId());
							} else {
								log.info("解析token为null");
								chektoken = false;
							}
						}
					} catch (Exception e) {
						// 有异常就是token解析失败
						e.printStackTrace();
						chektoken = false;
					}
				} else {
					log.info("传的token异常====" + authToken);
					chektoken = false;
				}
			}
		}

		if (((HttpServletRequest) request).getRequestURI().contains("files")
				|| ((HttpServletRequest) request).getRequestURI().contains("qrCode")
				|| ((HttpServletRequest) request).getRequestURI().contains("companyLogo")
				|| ((HttpServletRequest) request).getRequestURI().contains("shoppingGoodsImg")
				|| ((HttpServletRequest) request).getRequestURI().contains("couponImg")
				|| ((HttpServletRequest) request).getRequestURI().contains("pictureMaterialImg")
				|| ((HttpServletRequest) request).getRequestURI().contains("richTextImg")
				|| ((HttpServletRequest) request).getRequestURI().contains("aLiQRCode")
				|| ((HttpServletRequest) request).getRequestURI().contains("articleImg")
				|| ((HttpServletRequest) request).getRequestURI().contains("officialArticle/officialList")
				|| ((HttpServletRequest) request).getRequestURI().contains("officialMessage/add")
				|| ((HttpServletRequest) request).getRequestURI().contains("officialArticle/officialHomeList")
				|| ((HttpServletRequest) request).getRequestURI().contains("officialArticle/findDto")
				|| ((HttpServletRequest) request).getRequestURI().contains("messageImg")
				|| ((HttpServletRequest) request).getRequestURI().contains("vendingMachinesInfo/nearbyListPage")
				|| ((HttpServletRequest) request).getRequestURI().contains("vendingMachinesWay/listAllForMap")
				|| ((HttpServletRequest) request).getRequestURI().contains("vendingMachinesWay/listAllForMap2")
				|| ((HttpServletRequest) request).getRequestURI().contains("activityImg")
				|| ((HttpServletRequest) request).getRequestURI().contains("shoppingGoods/productDetails")
				|| ((HttpServletRequest) request).getRequestURI().contains("tblCustomerSpellGroup/list")
				|| ((HttpServletRequest) request).getRequestURI().contains("vmAdvertisingImg")
				|| ((HttpServletRequest) request).getRequestURI().contains("complainImg")
				|| ((HttpServletRequest) request).getRequestURI().contains("tblCustomerComplain/uploadImage")
				|| ((HttpServletRequest) request).getRequestURI().contains("bargainSponsor/bargainGoodsList")
				|| ((HttpServletRequest) request).getRequestURI().contains("bargain/getBargainInfo")
				|| ((HttpServletRequest) request).getRequestURI().contains("spellGroupSharer/list")
				|| ((HttpServletRequest) request).getRequestURI()
						.contains("vendingMachinesAdvertising/findVendingSlideshow")
				|| ((HttpServletRequest) request).getRequestURI()
						.contains("vendingMachinesAdvertising/showMachinesAdvertising")
				|| ((HttpServletRequest) request).getRequestURI().contains("wordUrl")
				|| ((HttpServletRequest) request).getRequestURI().contains("temporary") ) {
			chektoken = true;
		}
		if (!chektoken) {
			ModifyHttpServletRequestWrapper httpServletRequestWrapper = new ModifyHttpServletRequestWrapper(
					(HttpServletRequest) request);
			httpServletRequestWrapper.putHeader("Accept-Charset", "utf-8");
			httpServletRequestWrapper.putHeader("Content-type", "application/json; charset=utf-8");// 设置编码
			ReturnDataUtil returnData = new ReturnDataUtil();
			returnData.setStatus(-1);
			returnData.setMessage("请重新登录");
			ObjectMapper objectMapper = new ObjectMapper();
			String writeValueAsString = objectMapper.writeValueAsString(returnData);
			response.getWriter().write(writeValueAsString);
		} else {
			ModifyHttpServletRequestWrapper httpServletRequestWrapper = new ModifyHttpServletRequestWrapper(
					(HttpServletRequest) request);
			httpServletRequestWrapper.putHeader("Accept-Charset", "utf-8");
			httpServletRequestWrapper.putHeader("Content-type", "application/json; charset=utf-8");// 设置编码
			chain.doFilter(httpServletRequestWrapper, response);

		}
		// log.info("WebFilter---------doFilter------ end");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
