package com.steinerize.cloud.messaging.controllers;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.steinerize.cloud.messaging.controllers.PushController;
import com.steinerize.cloud.messaging.domain.Device;
import com.steinerize.cloud.messaging.domain.PushMessage;
import com.steinerize.cloud.messaging.domain.User;
import com.steinerize.cloud.messaging.services.push.PushService;

/**
 * @author jsteiner
 *
 */
public class PushControllerTest {
	
	PushController controller;
	@Mock
	PushService pushService;
	
	User user;
	PushMessage msg;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		controller = new PushController(pushService);
		msg = new PushMessage();
	}
	
	@Test
	public void register_should_call_pushService_register() {
		user = new User();
		
		controller.register(user);
		verify(pushService, Mockito.only()).register(user);
	}
	
	@Test
	public void unregister_should_call_pushService_unregister() {
		user = new User("my user", Device.ANDROID, "12345");
		
		controller.unregister(user);
		verify(pushService, Mockito.only()).unregister(user.name, user.token);
	}
	
	@Test
	public void send_should_call_pushService_send() {
		controller.send(msg);
		verify(pushService, Mockito.only()).send(msg);
	}
	
}
