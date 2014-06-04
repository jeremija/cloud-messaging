package com.steinerize.cloud.messaging.domain.cloud.google;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.steinerize.cloud.messaging.domain.PushMessage;
import com.steinerize.cloud.messaging.domain.cloud.CloudRequest;

/**
 * @author jsteiner
 *
 */
public class GcmRequest extends CloudRequest {
	
	private final GcmMessage data;
	
	@JsonProperty("registration_ids")
	private final List<String> registrationIds;
	
	public GcmRequest(String title, String message) {
		this.data = new GcmMessage(title, message);
		this.registrationIds = new ArrayList<String>(); 
	}
	
	public GcmRequest addRegistrationId(String id) {
		this.registrationIds.add(id);
		return this;
	}
	
	public GcmMessage getData() {
		return data;
	}

	public List<String> getRegistrationIds() {
		return registrationIds;
	}
	
	public static GcmRequest fromPushMessage(PushMessage pushMessage) {
		String title = pushMessage.data != null ? pushMessage.data.title : "";
		String msg = pushMessage.data != null ? pushMessage.data.message : "";

		GcmRequest req = new GcmRequest(title, msg);
		
		if (pushMessage.androidTokens != null) {
			req.getRegistrationIds().addAll(pushMessage.androidTokens);		
		}
		
		return req;
	}
	
}
