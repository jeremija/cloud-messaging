package com.steinerize.cloud.messaging.domain.cloud.google;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.steinerize.cloud.messaging.domain.cloud.CloudResponse;

/**
 * @author jsteiner
 *
 */
public class GcmResponse extends CloudResponse {
	
	public static class GcmResult {
		@JsonProperty("message_id")
		public String messageId;
		
		@JsonProperty("registration_id")
		public String regId;
		
		public String error;
	}
	
	
	@JsonProperty("multicast_id")
	public String multicastId;
	
	public int success;
	public int failure;
	
	@JsonProperty("canonical_ids")
	public int canonicalIds;
	
	public List<GcmResult> results;
	
}
