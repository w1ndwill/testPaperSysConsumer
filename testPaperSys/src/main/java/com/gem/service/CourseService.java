package com.gem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.entity.Course;
import com.gem.vo.CourseVo;
import com.gem.vo.TableVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 */
public interface CourseService extends IService< Course > {
	
	TableVo< CourseVo > queryListByPage(String courseName , int page , int pageSize);
	
	/**
	 * 根据题库id 获取课程名
	 *
	 * @param courseId
	 * @return
	 */
	String getCourseName(String courseId);
	
	/**根据课程id获取课程名
	 *
	 * @param courseId
	 * @return
	 */
	String getQueCourseName(String courseId);
	
	List< CourseVo> getCourseList();
}
