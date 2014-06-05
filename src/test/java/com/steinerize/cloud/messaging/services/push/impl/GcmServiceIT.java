package com.steinerize.cloud.messaging.services.push.impl;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.steinerize.cloud.messaging.config.PropertiesConfig;
import com.steinerize.cloud.messaging.domain.PushData;
import com.steinerize.cloud.messaging.domain.PushMessage;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmAuthData;
import com.steinerize.cloud.messaging.http.impl.JsonHttpRequestFactory;
import com.steinerize.cloud.messaging.services.push.CloudMessagingService;

/**
 * @author jsteiner
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
	classes={PropertiesConfig.class, 
		JsonHttpRequestFactory.class,
		GcmResponseHandler.class, 
		GcmAuthData.class, 
		GcmService.class
	}, 
	loader=AnnotationConfigContextLoader.class)
public class GcmServiceIT {
	
	@Autowired
	@Qualifier("gcmService")
	CloudMessagingService service;
	
	@Before
	public void setup() {
	}
	
	@Test
	public void send() {
		PushMessage message = new PushMessage();
		message.androidTokens = new ArrayList<String>();
		message.androidTokens.add("APA91bFOH2fa1YQiuuBJ1qLgnItFQbAYORX1AA_OUzhgqoyb5Oaz5hMq8gT0kUNi5k8gg56NTh0IoAIhVevcN1-E14i5XsgYJ8tHNnHaw0dy09AnvT5O2C5uQhP0sinkifpXchWTog6p1JOjBq7CGMb9HhqAGBlm53Tls8IhH663KVlne3p4j0k");
		
		message.data = new PushData();
		message.data.message = "Test message body";
		message.data.title = "Test title";
	
		service.send(message);
	}
	
}
