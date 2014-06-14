package com.steinerize.cloud.messaging.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.Assert;

import com.steinerize.cloud.messaging.dao.Repo;

/**
 * @author jsteiner
 *
 * @param <T>
 */
public class RepoMongo<T> implements Repo<T> {
	
	protected final MongoTemplate template;
	private final Class<T> entityType;

	/**
	 * @param mongoTemplate
	 * @param entityType
	 * @throws IllegalArgumentException if any of the parameters is null
	 */
	public RepoMongo(MongoTemplate mongoTemplate, Class<T> entityType) {
		Assert.notNull(mongoTemplate, "mongoTemplate must be defined");
		Assert.notNull(entityType, "entityType must be defined");
		
		this.template = mongoTemplate;
		this.entityType = entityType;
	}
	
	@Override
	public void save(T objectToSave) {
		this.template.save(objectToSave);
	}
	
	@Override
	public T findById(String id) {
		return this.template.findById(id, entityType);
	}

	@Override
	public List<T> findAll() {
		return template.findAll(entityType);
	}
	
	@Override
	public void remove(T objectToRemove) {
		template.remove(objectToRemove);
	}
	
}
