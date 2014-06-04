package com.steinerize.cloud.messaging.services.push.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.steinerize.cloud.messaging.domain.PushMessage;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmAuthData;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmRequest;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmResponse;
import com.steinerize.cloud.messaging.http.HttpReq;
import com.steinerize.cloud.messaging.http.impl.JsonHttpRequestFactory;

/**
 * @author jsteiner
 *
 */
public class GcmServiceTest {
	
	private static final String AUTHORIZATION_KEY = "AUTH_KEY_123";
	
	@Mock 
	GcmResponseHandler handler;
	
	@Mock
	JsonHttpRequestFactory jsonHttpRequestFactory;
	
	@Mock
	HttpReq httpReq;
	
	GcmService service;
	
	GcmAuthData authData;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		doReturn(httpReq)
			.when(jsonHttpRequestFactory).post(any(String.class));
		doReturn(httpReq).when(httpReq).addHeader(
				any(String.class), any(String.class));
		doReturn(httpReq).when(httpReq).setRequestBody(any(Object.class));
		doReturn(httpReq).when(httpReq).send();
		
		authData = new GcmAuthData(AUTHORIZATION_KEY);
		service = new GcmService(authData, jsonHttpRequestFactory, handler);
	}
	
	private PushMessage createPushMessage() {
		PushMessage message = new PushMessage();
		message.androidTokens = new ArrayList<String>();
		message.androidTokens.add("token123");
		return message;
	}
	
	@Test
	public void test_should_send_request_to_server_and_call_handler() {
		GcmResponse res = new GcmResponse();
		
		doReturn(200).when(httpReq).getResponseStatus();
		doReturn(res).when(httpReq).getResponseAsObject(GcmResponse.class);
		
		PushMessage message = createPushMessage();
		
		service.send(message);
		
		verify(jsonHttpRequestFactory, only()).post(GcmService.GCM_SEND_URL);
		verify(handler, only()).handle(eq(200), any(GcmRequest.class), eq(res));
	}
	
	@Test
	public void test_should_send_request_to_server_and_call_handler2() {
		doReturn(400).when(httpReq).getResponseStatus();
		PushMessage message = createPushMessage();
		
		service.send(message);
		
		verify(handler, only()).handle(
				eq(400), any(GcmRequest.class), isNull(GcmResponse.class));
	}
	
	@Test
	public void test_should_not_send_request_if_no_android_tokens() {
		PushMessage message = new PushMessage();
		message.androidTokens = null;
		
		service.send(message);
		
		verifyNoMoreInteractions(handler);
	}
	
}
