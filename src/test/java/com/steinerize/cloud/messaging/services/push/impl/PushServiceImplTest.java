package com.steinerize.cloud.messaging.services.push.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.steinerize.cloud.messaging.dao.UserRepo;
import com.steinerize.cloud.messaging.domain.Device;
import com.steinerize.cloud.messaging.domain.PushMessage;
import com.steinerize.cloud.messaging.domain.User;
import com.steinerize.cloud.messaging.domain.UserMessage;
import com.steinerize.cloud.messaging.services.push.PushService;

/**
 * @author jsteiner
 * 
 */
public class PushServiceImplTest {

	PushService service;
	@Mock
	UserRepo repo;
	@Mock
	GcmService gcmService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		service = new PushServiceImpl(repo, gcmService);
	}

	@Test
	public void register_should_persist_user() {
		User user = new User();

		service.register(user);

		verify(repo, only()).save(user);
	}

	@Test
	public void unregister_with_name_should_remove_user_from_repo_by_name() {
		service.unregister("name", null);
		verify(repo, only()).removeByName("name");
	}

	@Test
	public void unregister_with_token_should_remove_user_from_repo_by_token() {
		service.unregister(null, "token");
		verify(repo, only()).removeByToken("token");
	}

	@Test
	public void send_should_forward_msg_to_gcm() {
		PushMessage pushMessage = new PushMessage();
		service.send(pushMessage);

		verify(gcmService, times(1)).send(pushMessage);
		verify(gcmService, only()).send(pushMessage);
	}

	@Test
	public void sendToUser_should_send_to_android() {
		PushService spiedService = spy(service);
		UserMessage message = new UserMessage("test title", "test msg");
		
		Answer<Void> answer = new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				PushMessage msg = (PushMessage) invocation.getArguments()[0];
				
				assertNotNull(msg);
				assertNotNull(msg.data);
				assertEquals("test title", msg.data.title);
				assertEquals("test msg", msg.data.message);
				
				return null;
			}
		}; 
		
		doAnswer(answer).when(spiedService).send(any(PushMessage.class));
		
		spiedService.sendToUser(message);
		
		verify(spiedService, times(1)).sendToUser(message);
		verify(spiedService, times(1)).send(any(PushMessage.class));
	}
	
	@Test
	public void sendToUser_should_extract_different_platform_tokens() {
		UserMessage msg = new UserMessage("title", "msg");
		String[] names = {"user1", "user2", "user3"};
		msg.usernames = Arrays.asList(names);
		
		List<User> users = new ArrayList<>();
		users.add(new User("user1", Device.ANDROID, "android1"));
		users.add(new User("user2", Device.IOS, "ios1"));
		users.add(new User("user3", Device.ANDROID, "android2"));
		
		when(repo.findByNames(names)).thenReturn(users);
		
		PushService spiedService = spy(service);
		
		Answer<Void> answer = new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				PushMessage msg = (PushMessage) invocation.getArguments()[0];
				
				assertEquals(2, msg.androidTokens.size());
				assertEquals(1, msg.appleTokens.size());
				
				assertEquals("android1", msg.androidTokens.get(0));
				assertEquals("android2", msg.androidTokens.get(1));
				assertEquals("ios1", msg.appleTokens.get(0));
				
				return null;
			}
		};
		
		doAnswer(answer).when(spiedService).send(any(PushMessage.class));
		
		spiedService.sendToUser(msg);
		
		verify(spiedService, times(1)).send(any(PushMessage.class));
	}
}
