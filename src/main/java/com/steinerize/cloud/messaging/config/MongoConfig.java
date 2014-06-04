package com.steinerize.cloud.messaging.config;

import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;

/**
 * @author jsteiner
 *
 */
@Configuration
public class MongoConfig {
	
	public static final Log LOG = LogFactory.getLog(MongoConfig.class);
	
	@Autowired
	Properties dbProperties;
	
	@Bean 
	public MongoClient mongoClient() {
		
		String hostname = dbProperties.getProperty("cloud.messaging.db.host");
		int port = Integer.parseInt(
				dbProperties.getProperty("cloud.messaging.db.port"));
		
		String url = hostname + ":" + port;
		LOG.info("initializing mongoClient: " + url);
		
		try {
			return new MongoClient(hostname, port);			
		}
		catch(UnknownHostException e) {
			throw new IllegalStateException("Unable to instantiate " + 
					"mongoClient: " + url);
		}
	}
	
	@Bean
	public MongoDbFactory mongoDbFactory() {;
		String dbName = dbProperties.getProperty("cloud.messaging.db.name");
		
		LOG.info("initializing SimpleMongoDbFactory db: " + dbName);
		return new SimpleMongoDbFactory(mongoClient(), dbName);
	}
	
	@Bean
	public MongoTemplate mongoTemplate() {
		LOG.info("initializing MongoTemplate");
		return new MongoTemplate(mongoDbFactory());
	}
}
