package com.steinerize.cloud.messaging.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Message to be sent to a specific list of users, regardless of their devices
 * 
 * @author jsteiner
 * 
 */
public class UserMessage {
	
	/**
	 * Message contents
	 */
	public PushData data = new PushData();
	
	/**
	 * List of usernames to send the message to
	 */
	public List<String> usernames = new ArrayList<>();
	
	public UserMessage() {}
	
	public UserMessage(String title, String message) {
		this.data.title = title;
		this.data.message = message;
	}
	
	public void addUsername(String username) {
		if (usernames.indexOf(username) > -1) return;
		usernames.add(username);
	}
	
	public void removeUsername(String username) {
		usernames.remove(username);
	}

}
