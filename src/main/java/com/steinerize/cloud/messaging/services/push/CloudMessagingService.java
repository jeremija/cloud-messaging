package com.steinerize.cloud.messaging.services.push;

import com.steinerize.cloud.messaging.domain.PushMessage;

public interface CloudMessagingService {
	
	public void send(PushMessage message);
	
}
