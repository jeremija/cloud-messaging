package com.steinerize.cloud.messaging.http.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.kevinsawicki.http.HttpRequest;
import com.steinerize.cloud.messaging.domain.Device;
import com.steinerize.cloud.messaging.domain.User;
import com.steinerize.cloud.messaging.http.HttpReq;
import com.steinerize.cloud.messaging.http.HttpRequestFactory;
import com.steinerize.cloud.messaging.http.RequestType;
import com.steinerize.cloud.messaging.http.impl.JsonHttpRequest;

public class JsonHttpRequestTest {
	
	JsonHttpRequest req;
	HttpRequest httpRequest;
	
	private class TestHttpRequestFactory extends HttpRequestFactory {
		public TestHttpRequestFactory(RequestType requestType) {
			super(requestType);
		}

		@Override
		public HttpRequest create(String url) {
			httpRequest = Mockito.mock(HttpRequest.class);
			
			doReturn(httpRequest).when(httpRequest).accept(any(String.class));
			doReturn(httpRequest).when(httpRequest).contentType(any(String.class));
			doReturn(httpRequest).when(httpRequest).send(any(String.class));
			
			return httpRequest;
		}
	}
	
	@Before
	public void setup() {
		req = new JsonHttpRequest("http://test.url", 
				new TestHttpRequestFactory(RequestType.GET));
	}
	
	@Test
	public void addHeader_should_add_header_to_httpRequest() {
		HttpReq ret = req.addHeader("key", "val");	
		verify(httpRequest, only()).header("key", "val");
		assertEquals(req, ret);
		
		ret = req.addHeader("key2", 20);
		verify(httpRequest, times(1)).header("key2", 20);
		assertEquals(req, ret);
	}
	
	@Test
	public void setRequestBody_should_set_request_body_variable() {
		req.setRequestBody(null);
		assertEquals("request body should be empty", "", req.getRequestBody());
		
		User user = new User();
		user.name = "a-name";
		user.device = Device.ANDROID;
		
		req.setRequestBody(user);
		String body = req.getRequestBody();
		
		assertTrue("body should be a json", body.startsWith("{"));
		assertTrue("body should be a json", body.endsWith("}"));
		
		assertTrue("body should contain a-user-name", body.contains("a-name"));
		assertTrue("body should contain ANDROID", body.contains("ANDROID"));
	}
	
	@Test
	public void send_should_set_content_types_and_send_request_body() {
		req.setRequestBody(new User()).send();
		String bodyJson = req.getRequestBody();
		
		verify(httpRequest, times(1)).contentType("application/json");
		verify(httpRequest, times(1)).accept("application/json");
		verify(httpRequest, times(1)).send(bodyJson);
	}
	
	@Test
	public void getResponseStatus_should_return_httpRequest_code() {
		doReturn(200).when(httpRequest).code();
		assertEquals(200, req.getResponseStatus());
	}
	
	@Test
	public void getResponseBody_should_return_httpRequest_body() {
		String body = "{\"a\":\"b\"}";
		doReturn(body).when(httpRequest).body();
		assertEquals(body, req.getResponseBody());
	}
	
	@Test
	public void getResponseObject_should_reconstruct_object_from_json() 
	{
		User user = new User();
		user.name = "name of user";
		ObjectMapper mapper = new ObjectMapper();
		
		String json;
		try {
			json = mapper.writeValueAsString(user);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		doReturn(json).when(httpRequest).body();
		
		User user2 = req.getResponseAsObject(User.class);
		assertEquals("should recreate object from json", user.name, user2.name);
	}
	
}
