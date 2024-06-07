package com.gem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gem.entity.*;
import com.gem.service.*;
import com.gem.vo.CourseVo;
import com.gem.vo.chartVo;
import com.gem.vo.yData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * package: com.gem.controller
 * ClassName : CourseAnalysis
 * 描述:课程分析
 **/

@RestController
@CrossOrigin  //解决跨域问题
@RequestMapping("/CourseAnalysis")
public class CourseAnalysis {
	@Autowired
	private KnowledgeService knowledgeService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionChoiceService questionChoiceService;
	@Autowired
	private QuestionBankService questionBankService;
	@Autowired
	private TestPaperService testPaperService;
	
	/**
	 * 柱形图
	 * @return
	 */
	@RequestMapping("query")
	public chartVo query() {
		// 获取课程列表数据
		List< CourseVo > courseList = courseService.getCourseList();
		String[] courseNameArr = new String[courseList.size()];
		
		// 获取知识点数据
		int[] knowledgeArr = new int[courseList.size()];
		QueryWrapper< Knowledge > knowledgeQueryWrapper = new QueryWrapper<>();
		knowledgeQueryWrapper.groupBy("course_id");
		knowledgeQueryWrapper.orderByDesc("course_id");
		knowledgeQueryWrapper.select("course_id" , "count(*) as knowledgeDescribe");
		List< Knowledge > knowledgeList = knowledgeService.list(knowledgeQueryWrapper);
//		for ( int i = 0; i < courseList.size(); i++ ) {
//			int finalI = i;
//			knowledgeList.forEach(knowledge -> {
//				if ( courseList.get(finalI).getCourseId().equals(knowledge.getCourseId()) ){
//					knowledgeArr[finalI] = Integer.parseInt(knowledge.getKnowledgeDescribe());
//				}
//			});
//		}
		
		int[] queArr = new int[courseList.size()];
		int[] difficulty = new int[courseList.size()];
		//获取题目数据
		//1
		QueryWrapper< Question > questionQueryWrapper = new QueryWrapper<>();
		questionQueryWrapper.groupBy(" que_course_id");
		questionQueryWrapper.orderByDesc("que_course_id");
		questionQueryWrapper.select("que_course_id" , "count(*) as que_answer_describe" , "sum(que_difficulty) as que_difficulty");
		List< Question > questionList = questionService.list(questionQueryWrapper);
		//2
		QueryWrapper< QuestionChoice > questionChoiceQueryWrapper = new QueryWrapper<>();
		questionChoiceQueryWrapper.groupBy("que_course_id");
		questionChoiceQueryWrapper.orderByDesc("que_course_id");
		questionChoiceQueryWrapper.select("que_course_id" , "count(*) as que_answer_describe" , "sum(que_difficulty) as que_difficulty");
		List< QuestionChoice > questionChoicesList = questionChoiceService.list(questionChoiceQueryWrapper);
		
		for ( int i = 0; i < courseList.size(); i++ ) {
			String courseId = courseList.get(i).getCourseId();
			courseNameArr[i] = courseList.get(i).getCourseName();
			int finalI = i;
			knowledgeList.forEach(knowledge -> {
				if ( knowledge.getCourseId().equals(courseId) ) {
					knowledgeArr[finalI] = Integer.parseInt(knowledge.getKnowledgeDescribe());
				}
			});
			
			int difficultySum = 0;
			int temSum = 0;
			for ( Question question : questionList ) {
				if ( question.getQueCourseId().equals(courseId) ) {
					temSum += Integer.parseInt(question.getQueAnswerDescribe());
					difficultySum += question.getQueDifficulty();
				}
			}
			for ( QuestionChoice questionChoice : questionChoicesList ) {
				if ( questionChoice.getQueCourseId().equals(courseId) ) {
					temSum += Integer.parseInt(questionChoice.getQueAnswerDescribe());
					difficultySum += questionChoice.getQueDifficulty();
				}
			}
			queArr[i] = temSum;
			difficulty[i] = difficultySum > 0 ? difficultySum / temSum : 0;
		}
		List< yData > list = new ArrayList<>();
		list.add(new yData("知识点数量" , knowledgeArr));
		list.add(new yData("题目数量" , queArr));
		list.add(new yData("题目平均难度" , difficulty));
		return new chartVo(courseNameArr , list);
	}
	
	/**
	 * 两个饼状图
	 * @return
	 */
	@RequestMapping("getQueBankList")
	public chartVo getQueBankList() {
		// 获取课程列表数据
		List< CourseVo > courseList = courseService.getCourseList();
		String[] courseNameArr = new String[courseList.size()];
		//获取题库数据
		QueryWrapper< QuestionBank > questionBankQueryWrapper = new QueryWrapper<>();
		questionBankQueryWrapper.groupBy("que_bank_course_id");
		questionBankQueryWrapper.orderByDesc("que_bank_course_id");
		questionBankQueryWrapper.select("que_bank_course_id" , "count(*) as queBankDescribe");
		List< QuestionBank > questionBankList = questionBankService.list(questionBankQueryWrapper);
		
		// 获取试卷数量
		QueryWrapper< TestPaper > testPaperQueryWrapper = new QueryWrapper<>();
		testPaperQueryWrapper.groupBy("tp_course_id");
		testPaperQueryWrapper.orderByDesc("tp_course_id");
		testPaperQueryWrapper.select("tp_course_id" , "count(*) as tpDescribe");
		List< TestPaper > testPaperList = testPaperService.list(testPaperQueryWrapper);
		
		int[] queBankArr = new int[courseList.size()];
		int[] tpArr = new int[courseList.size()];
		for ( int i = 0; i < courseList.size(); i++ ) {
			String courseId = courseList.get(i).getCourseId();
			courseNameArr[i] = courseList.get(i).getCourseName();
			int finalI = i;
			//设置题库数量
			questionBankList.forEach(questionBank -> {
				if ( questionBank.getQueBankCourseId().equals(courseId) ) {
					queBankArr[finalI] = Integer.parseInt(questionBank.getQueBankDescribe());
				}
			});
			
			//设置试卷数量
			testPaperList.forEach(testPaper -> {
				if ( testPaper.getTpCourseId().equals(courseId) ) {
					tpArr[finalI] = Integer.parseInt(testPaper.getTpDescribe());
				}
			});
		}
		List< yData > list = new ArrayList<>();
		list.add(new yData("题库数量" , queBankArr));
		list.add(new yData("试卷数量" , tpArr));
		return new chartVo(courseNameArr , list);
	}
}
