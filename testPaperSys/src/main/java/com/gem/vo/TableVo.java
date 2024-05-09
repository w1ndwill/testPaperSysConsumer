package com.gem.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Date : 2021/1/21 11:06
 * package: com.gem.vo
 * ClassName : TableVO
 * 描述:
 *
 * @author QAQ
 */
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
