package com.steinerize.cloud.messaging.services.push.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.steinerize.cloud.messaging.config.MongoConfig;
import com.steinerize.cloud.messaging.config.PropertiesConfig;
import com.steinerize.cloud.messaging.dao.UserRepo;
import com.steinerize.cloud.messaging.dao.impl.UserRepoMongo;
import com.steinerize.cloud.messaging.domain.Device;
import com.steinerize.cloud.messaging.domain.User;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmAuthData;
import com.steinerize.cloud.messaging.http.impl.JsonHttpRequestFactory;
import com.steinerize.cloud.messaging.services.push.PushService;

/**
 * @author jsteiner
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
	classes={
		PropertiesConfig.class, 
		MongoConfig.class, 
		UserRepoMongo.class,
		JsonHttpRequestFactory.class,
		GcmResponseHandler.class,
		GcmAuthData.class, 
		GcmService.class, 
		PushServiceImpl.class
	}, 
	loader=AnnotationConfigContextLoader.class)
public class PushServiceIntegrationTest {
	
	private static final String USER_NAME = "test-user";
	
	@Autowired
	PushService pushService;
	
	@Autowired
	MongoTemplate template;
	
	@Autowired
	UserRepo userRepo;
	
	@Before
	public void setup() {
	}
	
	@After
	public void cleanup() {
		Query query = new Query()
			.addCriteria(new Criteria("name").is(USER_NAME));
		template.remove(query, User.class);
	}
	
	private User createUser(String token) {
		User user = new User();
		user.name = USER_NAME;
		user.device = Device.ANDROID;
		user.token = token;
		return user;
	}
	
	@Test
	public void should_autowire_service() {
		assertNotNull(pushService);
	}
	
	@Test
	public void register_should_persist_user() {
		User user = createUser("test-token");
		pushService.register(user);
		
		assertNotNull("user id", user.id);
		assertEquals("should persist user", 
				user.id,  userRepo.findByToken("test-token").id);
	}
	
	@Test
	public void register_should_overwrite_user_with_same_token() {
		User user1 = createUser("test-token");
		User user2 = createUser("test-token");
		
		pushService.register(user1);
		pushService.register(user2);
		
		List<User> users = userRepo.findByName(USER_NAME);
		assertEquals("should only find one user", 1, users.size());
		assertEquals("should persist only user 2", user2.id, users.get(0).id);
	}
	
	@Test
	public void register_should_add_user_with_different_token() {
		User user1 = createUser("test-token-1");
		User user2 = createUser("test-token-2");
		
		pushService.register(user1);
		pushService.register(user2);
		
		List<User> users = userRepo.findByName(USER_NAME);
		assertEquals("should find both users", 2, users.size());
		assertEquals("should persist user 1", user1.id, users.get(0).id);
		assertEquals("should persist user 1", user2.id, users.get(1).id);
	}
	
}
