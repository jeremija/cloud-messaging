package com.steinerize.cloud.messaging.services.push.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.steinerize.cloud.messaging.dao.UserRepo;
import com.steinerize.cloud.messaging.domain.Device;
import com.steinerize.cloud.messaging.domain.PushMessage;
import com.steinerize.cloud.messaging.domain.User;
import com.steinerize.cloud.messaging.domain.UserMessage;
import com.steinerize.cloud.messaging.services.push.PushService;

/**
 * @author jsteiner
 *
 */
@Service
public class PushServiceImpl implements PushService {
	
	private static final Log LOG = LogFactory.getLog(PushServiceImpl.class);

	private final UserRepo repository;
	
	private final GcmService gcmService;
	
	@Autowired
	public PushServiceImpl(UserRepo repository, GcmService gcmService) {
		this.repository = repository;
		this.gcmService = gcmService;
	}

	@Override
	public void register(User user) {
		this.repository.save(user);
	}

	@Override
	public void unregister(String name, String token) {
		if (StringUtils.hasText(token)) {
			this.repository.removeByToken(token);
			return;
		}
		this.repository.removeByName(name);
	}

	@Override
	public void send(PushMessage pushMessage) {
		gcmService.send(pushMessage);			
		LOG.warn("apple cloud notification service not yet implemented");
	}
	
	protected void processUser(PushMessage pushMessage, User user) {
		if (Device.ANDROID.equals(user.device)) 
			pushMessage.androidTokens.add(user.token);
		else if (Device.IOS.equals(user.device)) 
			pushMessage.appleTokens.add(user.token);
		else LOG.warn("user " + user.name + " had token for " + 
				user.device + ". no suitable token handler found");		
	}
	
	protected PushMessage convertToPushMessage(UserMessage userMessage) {
		PushMessage pushMessage = new PushMessage(userMessage.data);
		
		int count = userMessage.usernames.size();
		List<User> users = this.repository.findByNames(
				userMessage.usernames.toArray(new String[count]));
		
		for(User user : users) {
			processUser(pushMessage, user);
		}
		
		return pushMessage;
	}

	@Override
	public void sendToUser(UserMessage userMessage) {
		PushMessage pushMessage = convertToPushMessage(userMessage);
		send(pushMessage);
	}
	
}
