package com.gem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class chartVo {
	private String [] xData;
	private List<yData> yDataList;
	
}
