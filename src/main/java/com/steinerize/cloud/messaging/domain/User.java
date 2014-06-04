package com.steinerize.cloud.messaging.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;


/**
 * @author jsteiner
 *
 */
public class User {
	
	public User() {
		
	}
	
	public User(String name, Device device, String token) {
		this.name = name;
		this.device = device;
		this.token = token;
	}
	
	@Id public String id;
	@Indexed public String name;
	public Device device;
	@Indexed(unique = true) public String token;
}
