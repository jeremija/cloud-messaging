package com.steinerize.cloud.messaging.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.steinerize.cloud.messaging.dao.Repo;
import com.steinerize.cloud.messaging.dao.impl.RepoMongo;
import com.steinerize.cloud.messaging.domain.User;

/**
 * @author jsteiner
 *
 */
public class RepoMongoTest {
	
	Repo<User> repo;
	
	@Mock
	MongoTemplate template;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		repo = new RepoMongo<User>(template, User.class);
	}
	
	@Test
	public void findAll_should_call_template_findAll_and_return_users() {
		List<User> users = new ArrayList<User>();
		doReturn(users).when(template).findAll(User.class);
		
		List<User> returnedUsers = repo.findAll();
		
		verify(template, only()).findAll(User.class);
		assertEquals("should return users from template", users, returnedUsers);
	}
	
	@Test
	public void findById_should_call_template_findById_and_return() {
		String id = "123";
		User user = new User();
		doReturn(user).when(template).findById(id, User.class);
		
		User returnedUser = repo.findById(id);
		
		verify(template, only()).findById(id, User.class);
		assertEquals("should return users from template", user, returnedUser);
	}
	
	@Test
	public void save_should_call_template_save() {
		User user = new User();
		
		repo.save(user);
		
		verify(template, only()).save(user);
	}
	
	@Test
	public void remove_should_call_template_remove() {
		User user = new User();
		
		repo.remove(user);
		
		verify(template, only()).remove(user);
	}

}
