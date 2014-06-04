package com.steinerize.cloud.messaging.http;

import org.springframework.util.Assert;

import com.github.kevinsawicki.http.HttpRequest;

public class HttpRequestFactory {
	
	private final RequestType requestType;
	
	public HttpRequestFactory(RequestType requestType) {
		Assert.notNull(requestType, "requestType should be defined");
		this.requestType = requestType;
	}
	
	public HttpRequest create(String url) {
		
		if (RequestType.GET.equals(requestType)) 
			return new HttpRequest(url, "GET");
		if (RequestType.POST.equals(requestType))
			return new HttpRequest(url, "POST");
		
		throw new IllegalStateException("invalid request type " + requestType);
	}
	
}
