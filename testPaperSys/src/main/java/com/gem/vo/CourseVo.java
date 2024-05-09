package com.gem.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gem.entity.Course;
import com.gem.utils.ServiceUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

/**
 * Date : 2021/1/23 14:30
 * package: com.gem.vo
 * ClassName : CourseVo
 * 描述:
 *
 * @author QAQ
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CourseVo对象", description = "")
@ToString
@NoArgsConstructor
public class CourseVo {
	
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "课程id")
	@TableId(value = "course_id"/*, type = IdType.UUID*/)
	private String courseId;
	
	@ApiModelProperty(value = "课程名")
	private String courseName;
	
	@ApiModelProperty(value = "课程知识点个数")
	private Integer knowledgeNo;
	
	@ApiModelProperty(value = "课程描述")
	private String courseDescribe;
	
	@ApiModelProperty(value = "是否修改课程名")
	private String isChangeCourseName;
	
	public CourseVo(Course course) {
		this.courseId = course.getCourseId();
		this.courseName = course.getCourseName();
		this.knowledgeNo = ServiceUtil.getKnowledgeNo(course.getCourseId());
		this.courseDescribe = course.getCourseDescribe();
//		this.isChangeCourseName = false;
	}
	
	public static List< CourseVo > getCourseVoList(List< Course > courseList) {
		LinkedList< CourseVo > courseVos = new LinkedList<>();
		for ( Course course : courseList ) {
			courseVos.add(new CourseVo(course));
		}
		return courseVos;
	}
	
	public static Course getCourse(CourseVo courseVo) {
		Course course = new Course();
		course.setCourseName(courseVo.getCourseName());
		course.setCourseId(courseVo.getCourseId());
		course.setCourseDescribe(courseVo.getCourseDescribe());
//		course.setCourseKnowledgeIds();
		return course;
	}
}
