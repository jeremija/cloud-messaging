package com.steinerize.cloud.messaging.services.push.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.steinerize.cloud.messaging.dao.UserRepo;
import com.steinerize.cloud.messaging.domain.Device;
import com.steinerize.cloud.messaging.domain.PushMessage;
import com.steinerize.cloud.messaging.domain.User;
import com.steinerize.cloud.messaging.domain.UserMessage;
import com.steinerize.cloud.messaging.services.push.CloudMessagingService;
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
	private final AppleService appleService;
	
	public static int STATUS_ERROR_GCM = 1;
	public static int STATUS_ERROR_APPLE = 3;
	public static int STATUS_OK = 0;
	
	/**
	 * @param repository
	 * @param gcmService
	 * @param appleService
	 * @throws IllegalArgumentException if any of the parameters is null
	 */
	@Autowired
	public PushServiceImpl(UserRepo repository, GcmService gcmService, 
			AppleService appleService) {
		Assert.notNull(repository);
		Assert.notNull(gcmService);
		Assert.notNull(appleService);
		
		this.repository = repository;
		this.gcmService = gcmService;
		this.appleService = appleService;
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
	
	private int sendGCM(PushMessage pushMessage) {
		if (gcmService == null) {
			LOG.warn("GCM service disabled, skipping GCM send");
			return STATUS_OK;
		}
		
		return sendMessage(gcmService, pushMessage, STATUS_ERROR_GCM);
	}
	
	private int sendAPN(PushMessage pushMessage) {
		if (appleService == null) {
			LOG.warn("APN service disabled, skipping APN send");
			return STATUS_OK;
		}
		
		return sendMessage(gcmService, pushMessage, STATUS_ERROR_APPLE);
	}
	
	private int sendMessage(CloudMessagingService service, 
			PushMessage pushMessage, int errorCode) 
	{
		try {
			service.send(pushMessage);
			return STATUS_OK;
		}
		catch(Exception e) {
			LOG.error("Error sending GCM", e);
			return errorCode;
		}
	}

	@Override
	public int send(PushMessage pushMessage) {
		int gcmResult = sendGCM(pushMessage);
		int apnResult = sendAPN(pushMessage);
		
		return gcmResult | apnResult;
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
	public int sendToUser(UserMessage userMessage) {
		PushMessage pushMessage = convertToPushMessage(userMessage);
		return send(pushMessage);
	}
	
}
