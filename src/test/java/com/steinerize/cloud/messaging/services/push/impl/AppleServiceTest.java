package com.steinerize.cloud.messaging.services.push.impl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.steinerize.cloud.messaging.domain.PushData;
import com.steinerize.cloud.messaging.domain.PushMessage;
import com.steinerize.cloud.messaging.test.mockito.util.GenericAnswer;

public class AppleServiceTest {
	
	@Mock
	ApnsService apnsService;
	
	AppleService service;
	
	List<String> tokens = Arrays.asList("token1", "token2", "token3");
	
	PushMessage pushMessage = new PushMessage(
			new PushData("title-body", "msg-body"));
	
	@Before
	public void setup() {
		initMocks(this);
		
		service = new AppleService(apnsService);
		
		pushMessage.appleTokens = tokens;
	}
	
	
	@Test
	public void send_should_call_apnsService_push() {
		GenericAnswer<Collection<ApnsNotification>> answr = new GenericAnswer<>();
		when(apnsService.push(eq(tokens), any(String.class))).thenAnswer(answr);

		service.send(pushMessage);
		
		assertTrue("should have answered", answr.hasAnswered());
		String payload = (String) answr.getArguments()[1];
		
		assertTrue("should have set title", payload.contains("title-body"));
		assertTrue("should have set message", payload.contains("msg-body"));
		
		verify(apnsService, only()).push(tokens, payload);
	}
	
	@Test
	public void send_should_not_call_apnsService_push_if_no_apple_tokens() {
		pushMessage.appleTokens = new ArrayList<>();
		
		service.send(pushMessage);
		
		verifyNoMoreInteractions(apnsService);
	}
}
