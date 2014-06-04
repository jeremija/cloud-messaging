package com.steinerize.cloud.messaging.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.steinerize.cloud.messaging.domain.PushMessage;
import com.steinerize.cloud.messaging.domain.User;
import com.steinerize.cloud.messaging.services.push.PushService;

/**
 * @author jsteiner
 *
 */
@RequestMapping("/push")
public class PushController {
	
	private final PushService pushService;
	
	public PushController(PushService pushService) {
		this.pushService = pushService;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void register(@RequestBody User user) {
		this.pushService.register(user);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void unregister(@RequestBody User user) {
		this.pushService.unregister(user.name, user.token);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void send(@RequestBody PushMessage msg) {
		this.pushService.send(msg);
	}
}
