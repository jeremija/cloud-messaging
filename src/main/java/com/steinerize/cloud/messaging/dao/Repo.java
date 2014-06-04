package com.steinerize.cloud.messaging.dao;

import java.util.List;

/**
 * Serves as a wrapper around a persistent storage layer.
 * @author jsteiner
 *
 * @param <T>
 */
public interface Repo<T> {

	/**
	 * Saves object to persistent storage.
	 * @param objectToSave
	 */
	public void save(T objectToSave);
	
	/**
	 * Retrieves object by id
	 * @param id
	 * @return
	 */
	public T findById(String id);
	
	/**
	 * Finds all objects of type T.
	 * @return
	 */
	public List<T> findAll();
	
	/**
	 * Removes object from persistent storage.
	 * @param objectToRemove
	 */
	public void remove(T objectToRemove);

}