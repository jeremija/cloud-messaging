package com.steinerize.cloud.messaging.domain.cloud.google;

/**
 * @author jsteiner
 *
 */
public class GcmMessage {
	
	private String title;
	private String message;
	
	public GcmMessage(String title, String message) {
		this.title = title;
		this.message = message;
	}
	
	public String getTitle() {
		return title;
	}
	public String getMessage() {
		return message;
	}

}
