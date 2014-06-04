package com.steinerize.cloud.messaging.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.steinerize.cloud.messaging.dao.UserRepo;
import com.steinerize.cloud.messaging.domain.User;

/**
 * @author jsteiner
 *
 */
public class UserRepoMongoTest {

	UserRepo repo;
	@Mock 
	MongoTemplate template;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		repo = new UserRepoMongo(template);
	}
	
	
	@Test
	public void findByToken_should_call_template_findOne() {
		final User user = new User();
		
		QueryValidator<User> validator  = new QueryValidator<User>(user)
				.addPair("token", "12345");
		Answer<User> answer = validator.createAnswer();
		doAnswer(answer).when(template).findOne(any(Query.class), eq(User.class));
		
		User returnedUser = repo.findByToken("12345");
		
		verify(template, only()).findOne(any(Query.class), eq(User.class));
		assertEquals("should return found user", user, returnedUser);
	}
	
	@Test
	public void findByName_should_call_template_find() {
		List<User> users = new ArrayList<User>();
		
		Answer<List<User>> answer = new QueryValidator<List<User>>(users)
				.addPair("name", "a-name").createAnswer();
		doAnswer(answer).when(template).find(any(Query.class), eq(User.class));
		
		List<User> returnedUsers = repo.findByName("a-name");
		
		verify(template, only()).find(any(Query.class), eq(User.class));
		assertEquals("should return found users", users, returnedUsers);
	}
	
	@Test
	public void findByToken_should_call_template_find() {
		User user = new User();
		
		Answer<User> answer = new QueryValidator<User>(user)
				.addPair("token", "12345").createAnswer();
		doAnswer(answer).when(template).findOne(any(Query.class), eq(User.class));
		
		User returnedUser = repo.findByToken("12345");
		
		verify(template, only()).findOne(any(Query.class), eq(User.class));
		assertEquals("should return found user", user, returnedUser);
	}
	
	@Test
	public void removeByToken_should_call_template_remove() {
		String token = "12345";
		Answer<Void> answer = new QueryValidator<Void>(null)
				.addPair("token", token).createAnswer();
		doAnswer(answer).when(template).remove(any(Query.class), eq(User.class));
		
		repo.removeByToken(token);
		
		verify(template, only()).remove(any(Query.class), eq(User.class));
	}
	
	@Test
	public void findByNames_should_find_users_with_following_names() {
		final List<User> testUsers = new ArrayList<>();
		
		Answer<List<User>> answer = new Answer<List<User>>() {
			@Override
			public List<User> answer(InvocationOnMock invocation) 
					throws Throwable
			{
				Query query = (Query) invocation.getArguments()[0];
				
				Query query1 = new Query();
				Criteria[] or = {
						Criteria.where("name").is("name1"),
						Criteria.where("name").is("name2"),
						Criteria.where("name").is("name3")
				};
				query1.addCriteria(new Criteria().orOperator(or));
				
				assertEquals("query not validated", query1, query);
				
				return testUsers;
			}
		};
		
		doAnswer(answer).when(template).find(any(Query.class), eq(User.class));
		
		List<User> users = repo.findByNames("name1", "name2", "name3");
		
		assertEquals(testUsers, users);
	}
	
}
