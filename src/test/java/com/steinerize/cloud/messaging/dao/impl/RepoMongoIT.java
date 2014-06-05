package com.steinerize.cloud.messaging.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.StringUtils;

import com.steinerize.cloud.messaging.config.MongoConfig;
import com.steinerize.cloud.messaging.config.PropertiesConfig;
import com.steinerize.cloud.messaging.dao.Repo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		classes={PropertiesConfig.class, MongoConfig.class},
		loader=AnnotationConfigContextLoader.class)
public class RepoMongoIT {
	
	@Autowired
	MongoTemplate template;
	Repo<TestUser> repo;
	
	public static class TestUser {
		private String id;
		
		private String name;
		private String surname;
		private int age;
		
		public TestUser(String name, String surname, int age) {
			this.name = name;
			this.surname = surname;
			this.age = age;
		}
		
		public String getName() {
			return name;
		}
		public String getSurname() {
			return surname;
		}
		public int getAge() {
			return age;
		}
		public String getId() {
			return id;
		}
	}
	
	@Before
	public void setup() {
		repo = new RepoMongo<TestUser>(template, TestUser.class);
	}
	
	@After
	public void cleanup() {
		template.remove(new Query(), TestUser.class);
	}
	
	@Test
	public void save_should_persist_object() {
		TestUser user = new TestUser("test-name", "test-surname", 25);
		
		repo.save(user);
		
		assertTrue("should have set id", StringUtils.hasText(user.getId()));
		
		TestUser user1 = template.findById(user.id, TestUser.class);
		assertEquals("should have the same id", user.getId(), user1.getId());
	}
	
	@Test
	public void findAll_should_find_all_objects() {
		TestUser user0 = new TestUser("test-name-1", "test-surname-1", 25);
		TestUser user1 = new TestUser("test-name-2", "test-surname-2", 26);
		TestUser user2 = new TestUser("test-name-3", "test-surname-3", 27);
		
		repo.save(user0);
		repo.save(user1);
		repo.save(user2);
		
		List<TestUser> users = repo.findAll();
		assertEquals("should return all users", users.size(), 3);
	}
	
	@Test
	public void findById_should_find_specific_object() {
		TestUser user = new TestUser("test-name-1", "test-surname-1", 25);
		repo.save(user);
		assertTrue("should have set id", StringUtils.hasText(user.id));
		
		TestUser user2 = repo.findById(user.id);
		assertEquals("should have same name", "test-name-1", user2.name);
	}
	
	@Test
	public void remove_should_remove_object() {
		TestUser user = new TestUser("test-name-1", "test-surname-1", 25);
		repo.save(user);
		assertTrue("should have set id", StringUtils.hasText(user.id));
		
		TestUser user1 = repo.findById(user.id);
		assertEquals("should retrieve saved object", user.id, user1.id);
		
		repo.remove(user);
		TestUser user2 = repo.findById(user.id);
		assertNull("should not be able to retrieve user after removing", user2);
	}
}
