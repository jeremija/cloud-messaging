package com.steinerize.cloud.messaging.util;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.steinerize.cloud.messaging.domain.Device;
import com.steinerize.cloud.messaging.domain.User;
import com.steinerize.cloud.messaging.util.GenericHandler.Processor;

/**
 * @author jsteiner
 *
 */
public class GenericHandlerTest {
	
	@Mock
	Processor<User> processorAndroid;
	
	@Mock
	Processor<User> processorApple;
	
	GenericHandler<Device, User> handler;
	
	User user;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		 
		handler = new GenericHandler<>();
		
		handler.addHandler(Device.ANDROID, processorAndroid);
		handler.addHandler(Device.IOS, processorApple);
		
		user = new User();
	}
	
	@Test
	public void handle_null() {
		handler.handle(null, user);
		
		verifyNoMoreInteractions(processorAndroid);
		verifyNoMoreInteractions(processorApple);
	}
	
	@Test
	public void handle_android() {
		handler.handle(Device.ANDROID, user);
		
		verify(processorAndroid, only()).process(user);
		verifyNoMoreInteractions(processorApple);
	}
	
	@Test
	public void handle_apple() {
		handler.handle(Device.IOS, user);
		
		verifyNoMoreInteractions(processorAndroid);
		verify(processorApple, only()).process(user);
	}
	
}
