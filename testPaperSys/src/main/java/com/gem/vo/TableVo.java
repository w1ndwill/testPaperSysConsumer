package com.gem.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class TableVo< T > {
	/**
	 *  表格当前页数据
	 */
	private List< T > dataList;
	/**
	 *  总条数
	 */
	private Long pageTotal;
	
	/**
	 * 将数据转化成前端表格需要的格式
	 *
	 * @param dataList
	 * @param pageTotal
	 */
	public TableVo(List< T > dataList , Long pageTotal) {
		this.dataList = dataList;
		this.pageTotal = pageTotal;
	}

	public List<T> getData() {
		return dataList;
	}

	public long getTotal() {
		return pageTotal;
	}
}
