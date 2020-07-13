package com.server.filter;

import com.alibaba.fastjson.JSON;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class ModifyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String> customHeaders;

    private Map<String , String[]> params = new HashMap<String, String[]>();


    public ModifyHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<String, String>();

        //将参数表，赋予给当前的Map以便于持有request中的参数
        Map<String, String[]> requestMap=request.getParameterMap();
        System.out.println("转化前参数："+JSON.toJSONString(requestMap));
        this.params.putAll(requestMap);
        this.modifyParameterValues();
        System.out.println("转化后参数："+ JSON.toJSONString(params));
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    public String getHeader(String name) {
        String value = this.customHeaders.get(name);
        if (value != null) {
            return value;
        }
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<String>(customHeaders.keySet());
        Enumeration<String> enumeration = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            set.add(name);
        }
        return Collections.enumeration(set);
    }
    /**
     * 将parameter的值去除空格后重写回去
     */
    public void modifyParameterValues(){
        Set<String> set =params.keySet();
        Iterator<String> it=set.iterator();
        while(it.hasNext()){
            String key= (String) it.next();
            String[] values = params.get(key);
            values[0] = values[0].trim();
            params.put(key, values);
        }
    }


    /**
     * 重写getParameter 参数从当前类中的map获取
     */
    @Override
    public String getParameter(String name) {
        String[]values = params.get(name);
        if(values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }
    /**
     * 重写getParameterValues
     */
    public String[] getParameterValues(String name) {//同上
        return params.get(name);
    }
}
