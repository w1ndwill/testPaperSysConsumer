package com.gem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Date : 2021/3/6 13:29
 * package: com.gem.vo
 * ClassName : chartVo
 * 描述: echarts 插件所需数据Vo
 **/
@Data
@AllArgsConstructor
public class chartVo {
	private String [] xData;
	private List<yData> yDataList;
	
}
