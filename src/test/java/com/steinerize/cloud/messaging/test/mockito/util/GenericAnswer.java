package com.steinerize.cloud.messaging.test.mockito.util;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Helps with reducing boiler-plate code when implementing {@link Answer} when
 * you just want to check arguments of method invocation.
 * @author jsteiner
 * @param <T> return type of answer
 */
public class GenericAnswer<T> implements Answer<T> {

	private final T returnObject;
	private Object[] arguments;
	
	/**
	 * Creates a new answer instance which returns returnObject.
	 * @param returnObject
	 */
	public GenericAnswer(T returnObject) {
		this.returnObject = returnObject;
	}
	
	/**
	 * Creates a new answer instance which returns null.
	 */
	public GenericAnswer() {
		this.returnObject = null;
	}
	
	@Override
	public T answer(InvocationOnMock invocation) throws Throwable {
		this.arguments = invocation.getArguments();
		return returnObject;
	}
	
	/**
	 * @return -1 if arguments null, arguments count otherwise
	 */
	public int getLastArgumentsCount() {
		if (!hasAnswered()) return -1; 
		return arguments.length;
	}
	
	/**
	 * @return arguments of last invocation
	 */
	public Object[] getArguments() {
		return this.arguments;
	}
	
	/**
	 * @return true if arguments != 0, false otherwise
	 */
	public boolean hasAnswered() {
		return arguments != null;
	}
}
