package com.steinerize.cloud.messaging.config;

import java.util.Properties;

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
		return null;
	}
	
}
