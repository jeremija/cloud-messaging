package com.steinerize.cloud.messaging.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.steinerize.cloud.messaging.dao.UserRepo;
import com.steinerize.cloud.messaging.domain.User;

/**
 * @author jsteiner
 *
 */
@Service
public class UserRepoMongo extends RepoMongo<User> implements UserRepo {
	
	/**
	 * @param template
	 * @throws IllegalArgumentException if template is null
	 */
	@Autowired
	public UserRepoMongo(MongoTemplate template) {
		super(template, User.class);
	}
	
	@Override
	public void save(User user) {
		User existingUser = findByToken(user.token);
		if (existingUser != null) super.remove(existingUser);
		super.save(user);
	}

	@Override
	public User findByToken(String token) {
		Query query = new Query()
			.addCriteria(new Criteria("token").is(token));
		return template.findOne(query, User.class);
	}

	@Override
	public List<User> findByName(String name) {
		Query query = new Query()
			.addCriteria(new Criteria("name").is(name));
		return template.find(query, User.class);
	}
	
	private void remove(String key, String value) {
		Query query = new Query()
			.addCriteria(new Criteria(key).is(value));
		template.remove(query, User.class);
	}
	
	@Override
	public void removeByToken(String token) {
		remove("token", token);
	}

	@Override
	public void removeByName(String name) {
		remove("name", name);
	}

	@Override
	public List<User> findByNames(String... names) {
		Query query = new Query();
		
		Criteria[] namesCriteria = new Criteria[names.length];
		for(int i = 0; i < names.length; i++) {
			namesCriteria[i] = new Criteria("name").is(names[i]);
		}
		
		query.addCriteria(new Criteria().orOperator(namesCriteria));
		
		return template.find(query, User.class);
	}

}
