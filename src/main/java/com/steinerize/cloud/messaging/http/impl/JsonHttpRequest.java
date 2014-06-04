package com.steinerize.cloud.messaging.http.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.Assert;

import com.github.kevinsawicki.http.HttpRequest;
import com.steinerize.cloud.messaging.http.HttpReq;
import com.steinerize.cloud.messaging.http.HttpRequestFactory;

/**
 * Wrapper around {@link HttpRequest}. Simplifies writing JSON requests and
 * reconstructing objects from JSON responses.
 * @author jsteiner
 *
 */
public class JsonHttpRequest implements HttpReq {
	
	private final String resourceUrl;
	private final ObjectMapper mapper;
	private final HttpRequest request;
	
	private String requestBody;
	
	/**
	 * Creates a new request.
	 * @param resourceUrl
	 * @param reqFactory
	 */
	public JsonHttpRequest(String resourceUrl, HttpRequestFactory reqFactory) {
		Assert.hasText(resourceUrl, "resourceUrl must be defined");
		Assert.notNull(reqFactory, "reqFactory must be defined");
		
		this.resourceUrl = resourceUrl;
		this.mapper = new ObjectMapper();
		
		this.request = reqFactory.create(this.resourceUrl);
	}
	
	@Override
	public String getRequestBody() {
		return this.requestBody;
	}
	
	@Override
	public String getRequestMethod() {
		return this.request.method();
	}
	
	@Override
	public String getResourceUrl() {
		return this.resourceUrl;
	}
	
	@Override
	public HttpReq addHeader(String key, String value) {
		this.request.header(key, value);
		return this;
	}
	
	@Override
	public HttpReq addHeader(String key, int value) {
		this.request.header(key, value);
		return this;
	}
	
	@Override
	public HttpReq setRequestBody(Object data) {
		if (data == null) {
			this.requestBody = "";
			return this;
		}
		
		try {
			this.requestBody = mapper.writeValueAsString(data);
		}
		catch(Exception e) {
			throw new IllegalStateException("failed to convert object " + 
					data + " to JSON", e);
		}
		return this;
	}
	
	@Override
	public HttpReq send() {
		request.accept("application/json")
			.contentType("application/json")
			.send(requestBody);
		
		return this;
	}
	
	@Override
	public int getResponseStatus() {
		return request.code();
	}
	
	@Override
	public String getResponseBody() {
		return request.body();
	}
	
	@Override
	public <T> T getResponseAsObject(Class<T> clazz) {
		String json = getResponseBody();
		T value;
		try {
			value = mapper.readValue(json, clazz);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to convert json " + json +  
					" to " + clazz.getCanonicalName(), e);
		}
		return value;
	}

}
