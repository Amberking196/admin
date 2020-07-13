package com.server.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ParamsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1,
                         FilterChain arg2) throws IOException, ServletException {
        ModifyHttpServletRequestWrapper parmsRequest = new ModifyHttpServletRequestWrapper(
                (HttpServletRequest) arg0);
        arg2.doFilter(parmsRequest, arg1);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
