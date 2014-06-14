package com.steinerize.cloud.messaging.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.notnoop.apns.ApnsService;

@Configuration
public class CloudConfig {

	@Autowired
	Properties cloudProperties;
	
	@Bean
	public ApnsService apnsService() {
		ClassLoader classLoader = ApnsService.class.getClassLoader();
		
		Class<?>[] interfaces = new Class[] { ApnsService.class };
		
		InvocationHandler handler = new InvocationHandler() {
			private final Log LOG = LogFactory.getLog(ApnsService.class);
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable
			{
				LOG.warn("ApnsService#" + method.getName() + " not implemented");
				return null;
			}
		};
		
		return (ApnsService) Proxy.newProxyInstance(
				classLoader, interfaces, handler);
	}
	
	
}
