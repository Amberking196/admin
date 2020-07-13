package com.server;
  
import java.util.ArrayList;
import java.util.List;

import javax.servlet.DispatcherType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.server.common.paramconfig.AlipayConfig;
import com.server.filter.ExportFilter;
import com.server.filter.ParamsFilter;
import com.server.filter.WebFilter;
import com.server.interceptor.UserInterceptor;

@Configuration
public class WebAppConfigurer 
        extends WebMvcConfigurerAdapter {

    @Autowired
    private WebFilter actionFilter ;
    @Autowired
    private AlipayConfig alipayConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/**"); 
        super.addInterceptors(registry);
    }
    //@Bean
    public FilterRegistrationBean parmsFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new ParamsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("paramsFilter");
        registration.setOrder(Integer.MAX_VALUE-1);
        return registration;
    }
    @Bean
    public FilterRegistrationBean   filterRegistrationBean() {
      FilterRegistrationBean registrationBean = new FilterRegistrationBean();
      registrationBean.setFilter(actionFilter);
      List<String> urlPatterns = new ArrayList<String>();
      urlPatterns.add("/*");
      registrationBean.setUrlPatterns(urlPatterns);
      registrationBean.setOrder(1);//设置该过滤器的优先级，数字越小，优先级越高
      return registrationBean;
    }
    @Bean
    public FilterRegistrationBean   getExportFilter() {
    	FilterRegistrationBean registrationBean = new FilterRegistrationBean( new ExportFilter());
    	List<String> urlPatterns = new ArrayList<String>();
    	urlPatterns.add("/*");//拦截所有
    	registrationBean.setUrlPatterns(urlPatterns);
    	registrationBean.setOrder(2);//设置该过滤器的优先级，数字越小，优先级越高
    	return registrationBean;
    }
 

   // @Bean
   /* public Connection druidDataSource() throws SQLException {
    	Connection db =openConnection(); 
        return db;
    }*/
    
    
   /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }*/
    
    @Bean
    public RequestContextListener addRequestContextListener(){
    	RequestContextListener request=new RequestContextListener();
    	return request;
    }
   
    @Override  
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/files/**").addResourceLocations("file:///"+alipayConfig.getItemImg());
	    registry.addResourceHandler("/qrCode/**").addResourceLocations("file:///"+alipayConfig.getLocation());
	    registry.addResourceHandler("/companyLogo/**").addResourceLocations("file:///"+alipayConfig.getCompanyLogo()); 
	    registry.addResourceHandler("/shoppingGoodsImg/**").addResourceLocations("file:///"+alipayConfig.getShoppingGoodsImg());
        registry.addResourceHandler("/couponImg/**").addResourceLocations("file:///"+alipayConfig.getCouponImgPath());
        registry.addResourceHandler("/pictureMaterialImg/**").addResourceLocations("file:///"+alipayConfig.getPictureMaterialImg());
        registry.addResourceHandler("/richTextImg/**").addResourceLocations("file:///"+alipayConfig.getRichTextImg());
        registry.addResourceHandler("/aLiQRCode/**").addResourceLocations("file:///"+alipayConfig.getAliLocation()); 
	    registry.addResourceHandler("/articleImg/**").addResourceLocations("file:///"+alipayConfig.getArticleImg());	
	    registry.addResourceHandler("/activityImg/**").addResourceLocations("file:///"+alipayConfig.getActivityImg());
	    registry.addResourceHandler("/vmAdvertisingImg/**").addResourceLocations("file:///"+alipayConfig.getVmAdvertisingImg());
	    registry.addResourceHandler("/complainImg/**").addResourceLocations("file:///"+alipayConfig.getComplainImg());
	    registry.addResourceHandler("/wordUrl/**").addResourceLocations("file:///"+alipayConfig.getWordUrl());
	    registry.addResourceHandler("/temporary/**").addResourceLocations("file:///"+alipayConfig.getTemporary());
    	
        registry.addResourceHandler("/swagger-ui.html/**").addResourceLocations("classpath:/META-INF/resources/");

        
        //将templates目录下的CSS、JS文件映射为静态资源，防止Spring把这些资源识别成thymeleaf模版
/*        registry.addResourceHandler("/templates/**.js").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/templates/**.css").addResourceLocations("classpath:/templates/");
        //其他静态资源
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //swagger增加url映射
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");*/

    }

 

}
