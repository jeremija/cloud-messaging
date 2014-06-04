package com.steinerize.cloud.messaging.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the tokens per platform for notification servers. Also holds the 
 * {@link PushMessage} data.
 * @author jsteiner
 *
 */
public class PushMessage {
	
	public PushMessage() {
		data = new PushData();
	};
	
	public PushMessage(PushData pushData) {
		this.data = pushData;
	}
	
	/**
	 * Data to push
	 */
	public PushData data;
	
	public List<String> androidTokens = new ArrayList<>();
	public List<String> appleTokens = new ArrayList<>();
	
	public String toString() {
		if (data == null) return "PushMessage: [null]";
		return "PushMessage: [" + data.toString() + "]";
	}
	
}
