package com.steinerize.cloud.messaging.domain;

public class PushData {
	
	public PushData() {
		this.message = "";
		this.title = "";
	}
	
	public PushData(String title, String message) {
		this.title = title;
		this.message = message;
	}
	
	/**
	 * Notification message
	 */
	public String message;
	/**
	 * Notification title
	 */
	public String title;

	@Override
	public String toString() {
		return "Notification: [" + title + ": " + message + "]";
	}
}
