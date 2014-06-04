package com.steinerize.cloud.messaging.services.push;

import com.steinerize.cloud.messaging.domain.PushMessage;

/**
 * Defines an interface for sending cloud notification messages to various
 * providers.
 * @author jsteiner
 *
 */
public interface CloudMessagingService {
	
	/**
	 * Send a message to a provider
	 * @param message
	 */
	public void send(PushMessage message);
	
}
