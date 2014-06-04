package com.steinerize.cloud.messaging.http.impl;

import org.springframework.stereotype.Component;

import com.steinerize.cloud.messaging.http.HttpReq;
import com.steinerize.cloud.messaging.http.HttpRequestFactory;
import com.steinerize.cloud.messaging.http.RequestType;

@Component
public class JsonHttpRequestFactory {
	
	private final HttpRequestFactory getHttpRequestFactory;
	private final HttpRequestFactory postHttpRequestFactory;
	
	public JsonHttpRequestFactory() {
		getHttpRequestFactory = new HttpRequestFactory(RequestType.GET);
		postHttpRequestFactory = new HttpRequestFactory(RequestType.POST);
	}
	
	public HttpReq get(String resourceUrl) {
		return new JsonHttpRequest(resourceUrl, getHttpRequestFactory);
	}
	
	public HttpReq post(String resourceUrl) {
		return new JsonHttpRequest(resourceUrl, postHttpRequestFactory);
	}
	
}
