package com.steinerize.cloud.messaging.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
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
	
	/**
	 * Database id
	 */
	@JsonIgnore
	@Id 
	public String id;
	/**
	 * User name
	 */
	@Indexed 
	public String name;
	
	/**
	 * Device associated with the {@link #token}
	 */
	public Device device;
	
	/**
	 * Token associated with the {@link #device}	
	 */
	@Indexed(unique = true) 
	public String token;
}
