package com.steinerize.cloud.messaging.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.steinerize.cloud.messaging.domain.PushMessage;
import com.steinerize.cloud.messaging.domain.User;
import com.steinerize.cloud.messaging.domain.UserMessage;
import com.steinerize.cloud.messaging.services.push.PushService;

/**
 * @author jsteiner
 *
 */
@Controller
@RequestMapping("/push")
public class PushController {
	
	private final PushService pushService;
	
	/**
	 * @param pushService
	 * @throws IllegalArgumentException if any of the parameters is null
	 */
	@Autowired
	public PushController(PushService pushService) {
		Assert.notNull(pushService, "pushService must be defined");
		this.pushService = pushService;
	}
	
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public @ResponseBody String test(@RequestBody String string) {
		return "your request was " + string;
	}
	
	/**
	 * Persists the {@link User} data. Entries with the same names are allowed
	 * because a single user can have more than one device.
	 * @see {@link PushService#register(String, String)}
	 * @param user
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public void register(@RequestBody User user) {
		this.pushService.register(user);
	}
	
	/**
	 * Deletes the {@link User} data from persistent storage. 
	 * @see PushService#unregister(String, String)
	 * @param user
	 */
	@RequestMapping(value = "unregister", method = RequestMethod.POST)
	public void unregister(@RequestBody User user) {
		this.pushService.unregister(user.name, user.token);
	}
	
	/**
	 * Sends a push message to specified user tokens.
	 * @see PushMessage#androidTokens
	 * @see PushMessage#appleTokens
	 * @see PushService#send(PushMessage)
	 * @param msg
	 */
	@RequestMapping(value = "send", method = RequestMethod.POST)
	public void send(@RequestBody PushMessage msg) {
		this.pushService.send(msg);
	}
	
	/**
	 * Sends a push message to specified users as defined in
	 * {@link UserMessage#usernames}.
	 * @see PushService#sendToUser(UserMessage)
	 * @param userMessage
	 */
	@RequestMapping(value = "sendToUser", method = RequestMethod.POST)
	public void sendToUser(@RequestBody UserMessage userMessage) {
		this.pushService.sendToUser(userMessage);
	}
}
