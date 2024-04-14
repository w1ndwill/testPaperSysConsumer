package com.gem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gem.entity.Course;
import com.gem.service.CourseService;
import com.gem.utils.RedisService;
import com.gem.utils.UUIDUtil;
import com.gem.vo.CourseVo;
import com.gem.vo.TableVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 */
@RestController
@CrossOrigin  //解决跨域问题
@RequestMapping("/course")
public class CourseController {
	@Autowired
	private CourseService courseService;
	@Autowired
	private RedisService redisService;
	
	/**
	 * 查询课程 根据课程设置的过滤条件进行分页
	 *
	 * @param course
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/queryListByPage")
	public TableVo< CourseVo > queryListByPage(Course course , String userId , int page , int pageSize) {
		if ( userId == null || !userId.equals(redisService.get(userId)) ) {
			return new TableVo< CourseVo >(null , -1L);
		}
		if ( course == null ) {
			return null;
		}
		return courseService.queryListByPage(course.getCourseName() , page , pageSize);
	}
	
	/**
	 * 添加课程
	 *
	 * @param course
	 * @return
	 */
	@PostMapping("/add")
	public Map< String, Object > addCourse(@RequestBody Course course) {
		HashMap< String, Object > map = new HashMap<>();
		if ( course == null ) {
			map.put("success" , false);
			map.put("msg" , "数据丢失,添加失败");
			return map;
		}
		QueryWrapper< Course > courseQueryWrapper = new QueryWrapper<>();
		courseQueryWrapper.eq("course_name" , course.getCourseName());
		if ( courseService.count(courseQueryWrapper) > 0 ) {
			map.put("success" , false);
			map.put("msg" , "该课程已存在,无法添加!");
			return map;
		}
		
		course.setCourseId(UUIDUtil.get());
		map.put("success" , courseService.save(course));
		map.put("courseId" , course.getCourseId());
		return map;
	}
	
	/**
	 * 删除课程 和 批量删除
	 *
	 * @param courseId
	 * @return
	 */
	@PostMapping("/del")
	public boolean delCourseByPk(String courseId) {
		if ( null == courseId ) {
			return false;
		}
		QueryWrapper< Course > courseQueryWrapper = new QueryWrapper<>();
		Object[] ids = courseId.split(",");
		courseQueryWrapper.in("course_id" , ids);
		
		//删除知识点
		//to do
		return courseService.remove(courseQueryWrapper);
	}
	
	/**编辑课程
	 * @param courseVo
	 * @return
	 */
	@PostMapping("/edit")
	public Map< String, Object > editCourse(@RequestBody CourseVo courseVo) {
		Course course = CourseVo.getCourse(courseVo);
		HashMap< String, Object > map = new HashMap<>();
		if ( null == course.getCourseId() ) {
			map.put("success" , false);
			map.put("msg" , "数据丢失,无法添加!");
			return map;
		}
		QueryWrapper< Course > courseQueryWrapper = new QueryWrapper<>();
		courseQueryWrapper.eq("course_name" , course.getCourseName());
		if ( "T".equals(courseVo.getIsChangeCourseName()) ) {
			if ( courseService.count(courseQueryWrapper) > 0 ) {
				map.put("success" , false);
				map.put("msg" , "该课程已存在,无法修改!");
				return map;
			}
		} else {
			course.setCourseName(null);
		}
		
		UpdateWrapper< Course > courseUpdateWrapper = new UpdateWrapper<>();
		courseUpdateWrapper.eq("course_id" , course.getCourseId());
		map.put("success" , courseService.update(course , courseUpdateWrapper));
		return map;
	}
	
	/**
	 * 获取课程列表
	 * @return
	 */
	@RequestMapping("/getCourse")
	public List< CourseVo > getCourse() {
		return courseService.getCourseList();
	}
	
	/**
	 * 获取课程数量
	 * @return
	 */
	@RequestMapping("getCourseNo")
	public Integer getCourseNo(){
		return courseService.count();
	}
}

