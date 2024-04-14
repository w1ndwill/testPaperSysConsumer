package com.gem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.entity.Course;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 */
public interface CourseMapper extends BaseMapper< Course > {
	/**
	 * 根据题库id获取课程名
	 *
	 * @param queBankId
	 * @return
	 */
	String getCourseName(String queBankId);
}
