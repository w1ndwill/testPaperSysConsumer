package com.gem.utils;

import java.util.Arrays;
import java.util.List;

/**
 * package: com.gem.utils
 * ClassName : StringUtil
 * 描述:
 **/
public class StringUtil {
	
	/**
	 * 字符串 `[..]` 转成数组arr
	 * @param arrStr
	 * @return
	 */
	public static String[] arrStrToArr(String arrStr){
		 arrStr = trimBracket(arrStr).replace(" ", "");
		return arrStr.split(",");
	}
	public  static List<String> arrStrToList(String arrStr){
		String[] arr = arrStrToArr(arrStr);
//		LinkedList< String > list = new LinkedList<>();
//		list.addAll();
		return Arrays.asList(arr);
	}
	
	/**
	 * 去除 [..] 的前后中括号[]
	 * @param arrStr
	 * @return
	 */
	public static String trimBracket(String arrStr){
		if ( arrStr.contains("[")&& arrStr.contains("]")){
			arrStr = arrStr.substring(arrStr.indexOf("[")+1, arrStr.indexOf("]"));
		}
		return arrStr;
	}
	
	/**
	 * 检测是否为空,为空就返回true
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		try{
			return str.isEmpty();
		}catch (NullPointerException e){
			return true;
		}
	}
	/**
	 * 检测是否为空,为空就返回true
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(Object object){
		try{
			return object == null;
		}catch (NullPointerException e){
			return true;
		}
	}
	
}
