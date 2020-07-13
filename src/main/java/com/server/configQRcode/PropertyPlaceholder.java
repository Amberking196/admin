package com.server.configQRcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.server.jwt.TokenAuthenticationService;
/**
 * 
 * author name: why
 * create time: 2018-04-11 11:03:12s
 */
public class PropertyPlaceholder extends PropertyPlaceholderConfigurer {

	public static Logger log = LogManager.getLogger( PropertyPlaceholder.class);
	private static Map<String, String> propertyMap;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		propertyMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			propertyMap.put(keyStr, value);
		}
		logger.info(propertyMap);
	}

	// static method for accessing context properties
	public static String getString(String name) {
		return propertyMap.get(name);
	}
	public static int getInt(String name) {
		return Integer.valueOf(propertyMap.get(name));
	}
	public static Map<String, String> getAll() {
		return propertyMap;
	}
}