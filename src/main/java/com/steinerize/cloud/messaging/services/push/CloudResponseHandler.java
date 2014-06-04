package com.steinerize.cloud.messaging.services.push;

import com.steinerize.cloud.messaging.domain.cloud.CloudRequest;
import com.steinerize.cloud.messaging.domain.cloud.CloudResponse;

/**
 * @author jsteiner
 *
 */
public interface CloudResponseHandler {
	
	public void handle(int status, CloudRequest req, CloudResponse res);
	
}
