package com.steinerize.cloud.messaging.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ArrayUtilTest {
	
	@Test
	public void isEmpty() {
		assertTrue("should return true on null", ArrayUtil.isEmpty(null));
		assertTrue("should return true on empty list", 
				ArrayUtil.isEmpty(new ArrayList<Object>()));
		
		List<Object> list = new ArrayList<Object>();
		list.add(new Object());
		assertFalse("should return false on non-empty list", 
				ArrayUtil.isEmpty(list));
	}
	
	@Test
	public void hasElements() {
		assertFalse("should return false on null", ArrayUtil.hasElements(null));
		assertFalse("should return false on empty list", 
				ArrayUtil.hasElements(new ArrayList<Object>()));
		
		List<Object> list = new ArrayList<Object>();
		list.add(new Object());
		assertTrue("should return truet on non-empty list", 
				ArrayUtil.hasElements(list));
	}
	
}
