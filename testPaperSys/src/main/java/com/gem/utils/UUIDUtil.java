package com.gem.utils;

import java.util.UUID;

/**
 * UUID生成器
 * @author Administrator
 *
 */
public class UUIDUtil { // 生成器Generator，比较器Comparator
	
	private UUIDUtil(){
		
	}
	
	/**
	 * 生成uuid
	 * @return
	 */
	public static String get(){
		return UUID.randomUUID().toString().replace("-", "").substring(0, 18);
	}
	
}
