package com.steinerize.cloud.messaging.services.push.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.steinerize.cloud.messaging.dao.UserRepo;
import com.steinerize.cloud.messaging.domain.Device;
import com.steinerize.cloud.messaging.domain.User;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmRequest;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmResponse;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmResponse.GcmResult;
import com.steinerize.cloud.messaging.test.mockito.util.GenericAnswer;

public class GcmResponseHandlerTest {
	
	GcmResponseHandler handler;
	GcmRequest req;
	GcmResponse res;
	
	@Mock
	UserRepo userRepo;
	
	@Before
	public void setup() {
		initMocks(this);
		
		handler = new GcmResponseHandler(userRepo);
		
		req = new GcmRequest("tajtl", "msg");
		req.addRegistrationId("token1");
		req.addRegistrationId("token2");
		req.addRegistrationId("token3");
	}
	
	@Test
	public void handle_status_400() {
		handler.handle(400, req, null);
		
		verifyNoMoreInteractions(userRepo);
	}
	
	@Test
	public void handle_status_200_should_throw_exception_if_res_null() {
		try {
			handler.handle(200, req, null);
		}
		catch(IllegalStateException e) {
			return;
		}
		throw new IllegalStateException("should have thrown ISE");
	}
	
	@Test
	public void handle_status_200_should_do_nothing_if_no_res_failure() {
		GcmResponse res = new GcmResponse();
		res.failure = 0;
		res.canonicalIds = 0;
		
		handler.handle(200, req, res);
		verifyNoMoreInteractions(userRepo);
	}
	
	private GcmResponse createGcmResponse() {
		GcmResponse res = new GcmResponse();
		res.failure = 1;
		res.canonicalIds = 1;
		
		GcmResult result1 = new GcmResult();
		result1.error = null;
		result1.messageId = "msgId";
		result1.regId = null;
		
		GcmResult result2 = new GcmResult();
		result2.error = "error";
		result2.messageId = "msgId";
		result2.regId = "token4";
		
		GcmResult result3 = new GcmResult();
		result3.error = null;
		result3.messageId = "msgId";
		result3.regId = null;
		
		res.results = Arrays.asList(result1, result2, result3);
		
		return res;
	}
	
	@Test
	public void handle_status_200_should_replace_user_token_if_canonical() {
		res = createGcmResponse();
		
		User user = new User("user1", Device.ANDROID, "token2");
		GenericAnswer<User> answer = new GenericAnswer<>(user);
		doAnswer(answer).when(userRepo).findByToken("token2");
		
		handler.handle(200, req, res);
		
		verify(userRepo, times(1)).findByToken("token2");
		assertEquals("should update token2 to token4", "token4", user.token);
		verify(userRepo, times(1)).save(user);
		verifyNoMoreInteractions(userRepo);
	}
	
	@Test
	public void handle_status_200_should_do_nothing_if_unavailable_error() {
		res = createGcmResponse();
		res.results.get(1).regId = null;
		res.results.get(1).error = "Unavailable";
		
		handler.handle(200, req, res);
		
		verifyNoMoreInteractions(userRepo);
	}
	
	@Test
	public void handle_status_200_should_delete_user_if_unknown_error() {
		res = createGcmResponse();
		res.results.get(1).regId = null;
		
		handler.handle(200, req, res);
		
		verify(userRepo, only()).removeByToken("token2");
	}
	
}
