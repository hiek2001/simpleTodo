package com.example.demo.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class ObjectUtil {
	
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		if (obj instanceof String) return obj == null || "".equals(obj.toString().trim());
		else if (obj instanceof List) return obj == null || ((List) obj).isEmpty();
		else if (obj instanceof Map) return obj == null || ((Map) obj).isEmpty();
		else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
		else return obj == null;
	}
}
