package com.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.logsManager.exportLog.ExportLogService;
import com.server.util.ReturnDataUtil;

public class ExportFilter implements Filter{
	public static Logger log = LogManager.getLogger(WebFilter.class);
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private ExportLogService exportLogServiceImpl;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext context = filterConfig.getServletContext(); 
		ApplicationContext ac = WebApplicationContextUtils .getWebApplicationContext(context); 
		adminUserService = ac.getBean(AdminUserService.class); 
		exportLogServiceImpl=ac.getBean(ExportLogService.class);
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("=========<ExportFilter>==========<doFilter>========start");
		//只对导出做调整
		HttpServletRequest req=(HttpServletRequest) request;
		//对请求路径进行拦截
		boolean falg=req.getRequestURI().contains("export")||req.getRequestURI().contains("expert")||req.getRequestURI().contains("Export");
		if(req.getRequestURI().contains("exportLog")) {//不进行处理
			falg=false;
		}
		ExportLogBean bean=new ExportLogBean();
		if(falg) {
		//对bean的数据进行封装
		Long userId = (Long) req.getAttribute(AdminConstant.LOGIN_USER_ID);
		ReturnDataUtil data = adminUserService.findUserById(userId);
		AdminUserBean loginUser = (AdminUserBean)data.getReturnObject();
		bean.setOperator(userId);//设置导出人
		bean.setOperatorName(loginUser.getName());//设置导出人姓名
		bean.setCompanyId((long)loginUser.getCompanyId());//设置导出人公司
		req.setAttribute("exportBean", bean);
		}
		chain.doFilter(req, response);//放行
		if(falg) {
		//对数据进行处理
		exportLogServiceImpl.add(bean);
		}
		log.info("=========<ExportFilter>==========<doFilter>========end");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
