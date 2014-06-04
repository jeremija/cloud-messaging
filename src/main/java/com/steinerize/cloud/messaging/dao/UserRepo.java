package com.steinerize.cloud.messaging.dao;

import java.util.List;

import com.steinerize.cloud.messaging.domain.User;

/**
 * @author jsteiner
 *
 */
public interface UserRepo extends Repo<User> {
	
	/**
	 * Finds {@link User} by {@link User#token}.
	 * @param token
	 * @return
	 */
	public User findByToken(String token);
	
	/**
	 * Finds all {@link User} which have the same {@link User#name}.
	 * @param name
	 * @return
	 */
	public List<User> findByName(String name);
	
	/**
	 * Finds all {@link User} which have the specified {@link User#name}s.
	 * @param name
	 * @return
	 */
	public List<User> findByNames(String... names);
	
	/**
	 * Removes {@link User} with specific {@link User#token}.
	 * @param token
	 */
	public void removeByToken(String token);
	
	/**
	 * Removes all {@link User}s with specific (@link {@link User#name}.
	 * @param name
	 */
	public void removeByName(String name);
}
