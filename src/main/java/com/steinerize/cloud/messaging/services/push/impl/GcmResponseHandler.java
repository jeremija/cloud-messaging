package com.steinerize.cloud.messaging.services.push.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.steinerize.cloud.messaging.domain.cloud.CloudRequest;
import com.steinerize.cloud.messaging.domain.cloud.CloudResponse;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmRequest;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmResponse;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmResponse.Result;
import com.steinerize.cloud.messaging.services.push.CloudResponseHandler;

@Component
public class GcmResponseHandler implements CloudResponseHandler {
	
	private static final Log LOG = LogFactory.getLog(GcmResponseHandler.class);
	
	private void checkResponse(GcmResponse res) {
		if (res == null) {
			throw new IllegalStateException("status=200, but gcmResponse null");
		}		
	}
	
	private void processResponse(GcmRequest req, GcmResponse res) {
		for (int i = 0; i < res.results.size(); i++) {
			Result result = res.results.get(i);
			String regId = req.getRegistrationIds().get(i);
			
			checkForErrors(regId, result);
		}		
	}
	
	private void checkForErrors(String regId, Result result) {
		if (result.messageId != null && result.regId != null) {
			LOG.warn("replacing " + regId + " with " + result.regId);
			// TODO replace database regId with result.registratoinIds
			return;
		}
		
		if (result.error == null) {
			return;
		}
		
		LOG.error("Unable to send push msg to regId=" + regId + ". " + 
				"Error=" + result.error);
		
		if ("Unavailable".equals(result.error)) {
			return;
		}
			
		// TODO remove this regId from databases
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
