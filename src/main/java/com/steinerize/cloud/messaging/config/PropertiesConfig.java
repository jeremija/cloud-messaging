package com.steinerize.cloud.messaging.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

@Configuration
public class PropertiesConfig {
	
	private Properties loadProperties(String classpath) {
		Resource resource = new ClassPathResource(classpath);
		
		try {
			return PropertiesLoaderUtils.loadProperties(resource);			
		}
		catch(IOException e) {
			throw new IllegalStateException("unable to load /db.properties", e);
		}
	}
	
	@Bean
	public Properties cloudProperties() {
		return loadProperties("/cloud.properties");
	}
	
	@Bean
	public Properties dbProperties() {
		return loadProperties("/db.properties");
	}
	
	@Bean
	public String gcmAuthorizationKey() {
		Properties props = cloudProperties();
		return props.getProperty("gcm.api.key");
	}
}
