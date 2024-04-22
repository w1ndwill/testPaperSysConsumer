package com.gem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.entity.Course;
import com.gem.mapper.CourseMapper;
import com.gem.service.CourseService;
import com.gem.vo.CourseVo;
import com.gem.vo.TableVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 */
@Service
public class CourseServiceImpl extends ServiceImpl< CourseMapper, Course > implements CourseService {
	@Autowired(required = false)
	private CourseMapper courseMapper;
	
	@Override
	public TableVo< CourseVo > queryListByPage(String courseName , int page , int pageSize) {
		Page< Course > coursePage = new Page<>(page , pageSize);
		QueryWrapper< Course > courseQueryWrapper = new QueryWrapper<>();
		if ( courseName.length() > 0 ) {
			StringBuffer stringBuffer = new StringBuffer("%");
			for ( int i = 0; i < courseName.length(); i++ ) {
				stringBuffer.append(courseName.charAt(i)).append("%");
			}
			courseQueryWrapper.like("course_name" , stringBuffer);
		}
		courseQueryWrapper.orderByDesc("create_time");
		Page< Course > resultPage = courseMapper.selectPage(coursePage , courseQueryWrapper);
		return new TableVo<>(CourseVo.getCourseVoList(resultPage.getRecords()) , resultPage.getTotal());
	}
	
	@Override
	public String getCourseName(String courseId) {
		if ( courseId == null || "".equals(courseId) ) {
			return null;
		}
		QueryWrapper< Course > courseQueryWrapper = new QueryWrapper<>();
		courseQueryWrapper.eq("course_id" , courseId);
		System.out.println(courseMapper);
		Course course = courseMapper.selectOne(courseQueryWrapper);
		if ( null != course ) {
			return course.getCourseName();
		}
		return null;
	}
	
	@Override
	public String getQueCourseName(String courseId) {
		QueryWrapper< Course > courseQueryWrapper = new QueryWrapper<>();
		courseQueryWrapper.eq("course_id" , courseId);
		courseQueryWrapper.select("course_name");
		Course course = courseMapper.selectOne(courseQueryWrapper);
		if ( course == null ) {
			return "暂无";
		}
		return course.getCourseName();
	}
	
	@Override
	public List< CourseVo > getCourseList() {
		QueryWrapper< Course > courseQueryWrapper = new QueryWrapper<>();
		courseQueryWrapper.select("course_id" , "course_name");
		courseQueryWrapper.orderByDesc("course_id");
		return CourseVo.getCourseVoList(courseMapper.selectList(courseQueryWrapper));
	}
}
