package com.steinerize.cloud.messaging.services.push.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.steinerize.cloud.messaging.domain.PushMessage;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmAuthData;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmRequest;
import com.steinerize.cloud.messaging.domain.cloud.google.GcmResponse;
import com.steinerize.cloud.messaging.http.HttpReq;
import com.steinerize.cloud.messaging.http.impl.JsonHttpRequestFactory;
import com.steinerize.cloud.messaging.services.push.CloudMessagingService;
import com.steinerize.cloud.messaging.util.ArrayUtil;

/**
 * @author jsteiner
 *
 */
@Service
public class GcmService implements CloudMessagingService {

	private static final Log LOG = LogFactory.getLog(GcmService.class);
	
	public static final String GCM_SEND_URL = 
			"https://android.googleapis.com/gcm/send";
	
	private final GcmAuthData authData;
	private final GcmResponseHandler responseHandler;
	private final JsonHttpRequestFactory jsonHttpRequestFactory;
	
	@Autowired
	public GcmService(GcmAuthData authData, 
			JsonHttpRequestFactory jsonHttpRequestFactory, 
			GcmResponseHandler handler)
	{
		this.authData = authData;
		this.responseHandler = handler;
		this.jsonHttpRequestFactory = jsonHttpRequestFactory;
	}
	
	private HttpReq createHttpRequest(GcmRequest gcmRequest) {
		HttpReq httpReq = jsonHttpRequestFactory.post(GCM_SEND_URL)
				.addHeader("Authorization", "key=" + authData.getAuthorization())
				.setRequestBody(gcmRequest);
		
		return httpReq;
	}
	
	protected void sendGCM(PushMessage pushMessage) {
		GcmRequest gcmRequest = GcmRequest.fromPushMessage(pushMessage);
		HttpReq httpReq = createHttpRequest(gcmRequest);
		
		LOG.debug("send() requestBody=" + httpReq.getRequestBody());
		
		httpReq.send();
		
		int status = httpReq.getResponseStatus();
		
		LOG.debug("send() Response status=" + status);
		
		GcmResponse gcmResponse = null;
		if (status == 200) {
			gcmResponse = httpReq.getResponseAsObject(GcmResponse.class);
		}
		
		responseHandler.handle(status, gcmRequest, gcmResponse);		
	}

	@Override
	public void send(PushMessage pushMessage) {
		LOG.debug("send() START, pushMessage=" + pushMessage + 
				",  resourceUrl=" + GCM_SEND_URL);
		
		if (ArrayUtil.isEmpty(pushMessage.androidTokens)) {
			LOG.debug("send() skipping pushMessage - no android tokens");
			return;
		}
		
		sendGCM(pushMessage);
		
		LOG.debug("send() DONE");
	}
	
	
}
