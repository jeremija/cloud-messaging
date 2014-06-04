package com.steinerize.cloud.messaging.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jsteiner
 *
 * @param <Property> property for which to handle
 * @param <Type> type to handle
 */
public class GenericHandler<Property, Type> {
	
	private Map<Property, Processor<Type>> processors = 
			new HashMap<Property, Processor<Type>>();
	
	public void addHandler(Property key, Processor<Type> p) {
		processors.put(key, p);
	}
	
	public void removeHandler(Property key) {
		processors.remove(key);
	}
	
	public void handle(Property property, Type type) {
		Processor<Type> processor = processors.get(property);
		if (processor == null) return;
		processor.process(type);
	}
	
	public static interface Processor<H> {
		public void process(H object);
	}
	
	
}
