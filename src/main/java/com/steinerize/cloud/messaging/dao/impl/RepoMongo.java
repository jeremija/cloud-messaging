package com.steinerize.cloud.messaging.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.steinerize.cloud.messaging.dao.Repo;

/**
 * @author jsteiner
 *
 * @param <T>
 */
public class RepoMongo<T> implements Repo<T> {
	
	protected final MongoTemplate template;
	private final Class<T> entityType;

	public RepoMongo(MongoTemplate mongoTemplate, Class<T> entityType) {
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
