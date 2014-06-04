package com.steinerize.cloud.messaging.domain.cloud.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author jsteiner
 */
@Component
public class GcmAuthData {
	
	private final String authorization;
	
	@Autowired
	public GcmAuthData(@Qualifier("gcmAuthorizationKey") String authorization) {
		this.authorization = authorization;
	}
	public String getAuthorization() {
		return authorization;
	}
	
}
