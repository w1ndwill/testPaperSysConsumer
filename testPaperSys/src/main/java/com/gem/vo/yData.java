package com.gem.vo;

import lombok.Data;

import java.util.Random;

/**
 * Date : 2021/3/6 13:30
 * package: com.gem.vo
 * ClassName : yData
 * 描述:
 **/
@Data
public class yData {
	/**
	 * 数据列名
	 */
	private String name;
	/**
	 * 列数据
	 */
	private int[] data;
	/**
	 * 颜色
	 */
	private String color1;
	/**
	 * 渐变颜色
	 */
	private String color2;
	private static final Random random = new Random();
	public yData(String name,int[] data){
		this.name = name;
		this.data = data;
		this.color1 =getRandColor();
		this.color2 =getRandColor();
	}
	private String getRandColor() {
		int r =  random.nextInt(255);
		int g =  random.nextInt(255);
		int b =  random.nextInt(255);
		String color ="#"+Integer.toHexString(r)+Integer.toHexString(g)+Integer.toHexString(b);
		for ( int i=7-color.length();i>0;i-- ){
			color+="0";
		}
		return color;
	}
}
