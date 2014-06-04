package com.steinerize.cloud.messaging.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.DBObject;

/**
 * @author jsteiner
 *
 * @param <T>
 */
public class QueryValidator<T> {
	
	private final Map<String, String> pairs = new HashMap<String, String>();
	private final T mockedReturn;
	private final int queryArgumentIndex;
	private boolean validated = false;
	
	public QueryValidator(T mockedReturn) {
		this(mockedReturn, 0);
	}
	
	public QueryValidator(T mockedReturn, int queryArgumentIndex) {
		this.mockedReturn = mockedReturn;
		this.queryArgumentIndex = queryArgumentIndex;
	}

	public QueryValidator<T> addPair(String key, String value) {
		pairs.put(key, value);
		return this;
	}
	
	public QueryValidator<T> validate(Query query) {
		DBObject dbObject = query.getQueryObject();
		
		assertEquals("should have the same number of parameters",
				dbObject.keySet().size(), pairs.size());
		
		Iterator<Entry<String, String>> it = pairs.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			
			assertTrue("should have key \'" + key + "'", 
					dbObject.containsField(key));
			assertEquals("should have value '" + value + "' for key '" + 
					key + "'",  value, dbObject.get(key));
		}
		
		validated = true;
		return this;
	}
	
	public boolean isValidated() {
		return validated;
	}
	
	public Answer<T> createAnswer() {
		return new Answer<T>() {
			@Override
			public T answer(InvocationOnMock invocation) throws Throwable {
				Query query = (Query) 
						invocation.getArguments()[queryArgumentIndex];
				validate(query);
				return mockedReturn;
			}
		};
	}
	
}
