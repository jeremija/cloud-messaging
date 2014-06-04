package com.steinerize.cloud.messaging.services.push;

import com.steinerize.cloud.messaging.domain.PushMessage;
import com.steinerize.cloud.messaging.domain.User;
import com.steinerize.cloud.messaging.domain.UserMessage;

/**
 * @author jsteiner
 *
 */
public interface PushService {
	/**
	 * Persists the mobile user
	 * @param mobileUser
	 */
	public void register(User user);
	/**
	 * Deletes the mobile user from persistent storage.
	 * @param name user name
	 * @param token if null, will delete all entries with specific name.
	 */
	public void unregister(String name, String token);
	/**
	 * Pushes the message to specific device tokens
	 * @param pushMessage
	 */
	public void send(PushMessage pushMessage);
	
	/**
	 * Pushes the message to a specific user
	 * @param pushMessage
	 */
	public void sendToUser(UserMessage pushData);
}
