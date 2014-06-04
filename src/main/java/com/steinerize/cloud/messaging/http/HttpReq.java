package com.steinerize.cloud.messaging.http;


public interface HttpReq {

	/**
	 * Adds a header key-value pair to the request
	 * @param key
	 * @param value
	 * @return self
	 */
	public abstract HttpReq addHeader(String key, String value);

	/**
	 * Adds a header key-value pair to the request
	 * @param key
	 * @param value
	 * @return self
	 */
	public abstract HttpReq addHeader(String key, int value);

	/**
	 * Transforms data to JSON notation and stores it to a {@link String}
	 * variable. This variable will be used as a request body when 
	 * {@link #send()} is called.
	 * @param data
	 * @return
	 */
	public abstract HttpReq setRequestBody(Object data);

	/**
	 * Adds the following headers:
	 * <code><ul>
	 * <li>Accept: application/json</li>
	 * <li>ContentType: application/json</li>
	 * </ul></code>
	 * and sends the request to the {@link #resourceUrl}.
	 * @return self
	 */
	public abstract HttpReq send();

	/**
	 * Returns HTTP status code after the response is received.
	 * @return
	 */
	public abstract int getResponseStatus();

	/**
	 * Returns the response body as {@link String}.
	 * @return
	 */
	public abstract String getResponseBody();
	
	/**
	 * Returns the resource URL.
	 * @return
	 */
	public abstract String getResourceUrl();

	/**
	 * Attempts to reconstruct the object from JSON notation in response.
	 * @param clazz defines the object {@link Class} to be used in
	 * reconstruction.
	 * @return Reconstructed object
	 */
	public abstract <T> T getResponseAsObject(Class<T> clazz);
	
	/**
	 * Returns the request body
	 * @return
	 */
	public abstract String getRequestBody();
	
	/**
	 * Returns the request method
	 * @return
	 */
	public abstract String getRequestMethod();
	

}