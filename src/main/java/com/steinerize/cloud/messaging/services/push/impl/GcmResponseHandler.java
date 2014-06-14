package com.steinerize.cloud.messaging.services.push.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.steinerize.cloud.messaging.dao.UserRepo;
import com.steinerize.cloud.messaging.domain.User;
import com.steinerize.cloud.messaging.domain.cloud.CloudRequest;
import com.steinerize.cloud.messaging.domain.cloud.CloudResponse;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmRequest;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmResponse;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmResponse.GcmResult;
import com.steinerize.cloud.messaging.services.push.CloudResponseHandler;

@Component
public class GcmResponseHandler implements CloudResponseHandler {
	
	private static final Log LOG = LogFactory.getLog(GcmResponseHandler.class);
	
	private final UserRepo userRepo;
	
	/**
	 * @param userRepo
	 * @throws IllegalArgumentException if any of the parameters is null
	 */
	@Autowired
	public GcmResponseHandler(UserRepo userRepo) {
		Assert.notNull(userRepo, "userRepo must be defined");
		this.userRepo = userRepo;
	}
	
	/**
	 * @throws IllegalStateException if status == 200, but gcmResponse == null
	 * @param res
	 */
	private void checkResponse(GcmResponse res) {
		if (res == null) {
			throw new IllegalStateException("status=200, but gcmResponse null");
		}		
	}
	
	private void processResponse(GcmRequest req, GcmResponse res) {
		for (int i = 0; i < res.results.size(); i++) {
			GcmResult gcmResult = res.results.get(i);
			String token = req.getRegistrationIds().get(i);
			
			checkForErrors(token, gcmResult);
		}		
	}
	
	private void replaceUserToken(String token, String newToken) {
		User user = userRepo.findByToken(token);
		user.token = newToken;
		userRepo.save(user);
	}
	
	private void removeUser(String token) {
		userRepo.removeByToken(token);
	}
	
	private void checkForErrors(String token, GcmResult result) {
		if (result.messageId != null && result.regId != null) {
			LOG.warn("replacing " + token + " with " + result.regId);
			replaceUserToken(token, result.regId);
			return;
		}
		
		if (result.error == null) {
			return;
		}
		
		LOG.error("Unable to send push msg to regId=" + token + ". " + 
				"Error=" + result.error);
		
		if ("Unavailable".equals(result.error)) {
			return;
		}
			
		removeUser(token);
	}

	@Override
	public void handle(int status, CloudRequest request, CloudResponse response) {
		GcmRequest req = (GcmRequest) request;
		GcmResponse res = (GcmResponse) response;
		
		if (status != 200) {
			LOG.error("Error while sending notification. Status=" + status);
			return;
		}
		
		checkResponse(res);
		
		if (res.failure == 0 && res.canonicalIds == 0) {
			LOG.info("Success! Got status 200 and no errors");
			return;
		}
		
		LOG.error("Some errors were found. Iterating...");
		processResponse(req, res);
	}

}
