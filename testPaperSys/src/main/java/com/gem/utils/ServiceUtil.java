package com.gem.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gem.entity.Knowledge;
import com.gem.entity.Question;
import com.gem.entity.QuestionChoice;
import com.gem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * package: com.gem.utils
 * ClassName : ServiceUtil
 * 描述: 这是一个service层的工具类,调用各个service方法.
 * 为什么要写到这里来呢?因为
 * 在其他普通的java类里面使用@Autowired时候,得到的值为空,无法调用到service方法
 *
 * @author QAQ
 */
@Component
public class ServiceUtil {
	
	private static ServiceUtil serviceUtil;
	
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private KnowledgeService knowledgeService;
	
	@Autowired
	private QuestionBankService questionBankService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuestionChoiceService questionChoiceService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private KeyService keyService;

	@Autowired
	private PaperWdService paperWdService;
	
	/**
	 * 初始化该工具类
	 */
	@PostConstruct
	public void init() {
		serviceUtil = this;
	}
	
	/**
	 * 根据题库 id获取题库名
	 *
	 * @param queBankId
	 * @return
	 */
	public static String getQueBankName(String queBankId) {
		return serviceUtil.questionBankService.getQueBankName(queBankId);
	}
	
	/**
	 * 通过创建人(用户id) 获取用户名
	 *
	 * @param userId
	 * @return
	 */
	public static String getUserName(String userId) {
		return serviceUtil.userService.getUserName(userId);
	}
	
	/**
	 * 通过 知识点id列表 获取 知识点名字列表
	 *
	 * @param knowledgeIds
	 * @return
	 */
	public static String getKnowledgeName(String knowledgeIds) {
		return serviceUtil.knowledgeService.getKnowledgeNames(knowledgeIds);
	}
	
	/**
	 * 随机获取选择题
	 * @param choiceNo
	 * @return
	 */
	public static List< QuestionChoice > getChoiceQueList(String tpCourseId, Integer choiceNo) {
		return  serviceUtil.questionChoiceService.listByRand(tpCourseId,choiceNo);
		
	}
	
	/**
	 * 根据所属课程、题型和数量随机获取题目
	 * @param queNo
	 * @param queType
	 * @return
	 */
	public static List< Question > getQueList(String tpCourseId, Integer queNo , String queType) {
		return  serviceUtil.questionService.listByRand( tpCourseId,queNo,queType);
	}
	
	
	/**
	 * 获取课程名
	 *
	 * @param courseId
	 * @return
	 */
	public String getCourseName(String courseId) {
		return serviceUtil.courseService.getCourseName(courseId);
	}
	
	/**
	 * 根据课程id获取知识点个数
	 *
	 * @param courseId
	 * @return
	 */
	public static Integer getKnowledgeNo(String courseId) {
		QueryWrapper< Knowledge > knowledgeQueryWrapper = new QueryWrapper<>();
		knowledgeQueryWrapper.eq("course_id" , courseId);
		return serviceUtil.knowledgeService.count(knowledgeQueryWrapper);
	}
	
	/**
	 * 根据课程id获取课程名
	 *
	 * @param courseId
	 * @return
	 */
	public static String getQueCourseName(String courseId) {
		
		return serviceUtil.courseService.getQueCourseName(courseId);
	}
	
	/**
	 * 通过 ids 获取知识点列表
	 * @param tpKnowledgeIds
	 * @return
	 */
	public static List< Knowledge> getKnowledgeByIds(String tpKnowledgeIds) {
		QueryWrapper< Knowledge > knowledgeQueryWrapper = new QueryWrapper<>();
		knowledgeQueryWrapper.in("knowledge_id", StringUtil.arrStrToList(tpKnowledgeIds));
		return serviceUtil.knowledgeService.list(knowledgeQueryWrapper);
	}

	/**
	 * 通过 ids 获取密钥
	 * @param tpQuestionIds
	 * @return
	 */
//	public static List< String > getKeyByIds(String tpQuestionIds) {
//
//	}




	
}
