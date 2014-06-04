package com.steinerize.cloud.messaging.util;

import java.util.List;

public class ArrayUtil {
	
	public static boolean hasElements(List<?> list) {
		return !isEmpty(list);
	}
	
	public static boolean isEmpty(List<?> list) {
		return list == null ? true : list.size() == 0;
	}
	
}
