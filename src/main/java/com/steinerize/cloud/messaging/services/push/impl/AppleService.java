package com.steinerize.cloud.messaging.services.push.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.steinerize.cloud.messaging.domain.PushMessage;
import com.steinerize.cloud.messaging.services.push.CloudMessagingService;
import com.steinerize.cloud.messaging.util.ArrayUtil;

/**
 * @author jsteiner
 *
 */
@Service
public class AppleService implements CloudMessagingService {
	
	private static final Log LOG = LogFactory.getLog(AppleService.class);
	
	private final ApnsService apnsService; 
	
	/**
	 * @param apnsService
	 * @throws IllegalArgumentException if any of the parameters is null
	 */
	@Autowired
	public AppleService(ApnsService apnsService) {
		Assert.notNull(apnsService, "apnsService must be defined");
		this.apnsService = apnsService;
	}
	
	private String createPayload(PushMessage pushMessage) {
		return APNS.newPayload()
				.badge(1)
				.customField("title", pushMessage.data.title)
				.customField("message", pushMessage.data.message)
				.build();
	}
	
	@Override
	public void send(PushMessage pushMessage) {
		LOG.debug("send() START, pushMessage=" + pushMessage);
		
		List<String> deviceTokens = pushMessage.appleTokens;
		
		if (ArrayUtil.isEmpty(deviceTokens)) {
			return;
		}
		
		String payload = createPayload(pushMessage);
		
		LOG.debug("send() payload=" + payload);
		
		apnsService.push(deviceTokens, payload);
		
		LOG.debug("send() DONE");
	}
	
}
