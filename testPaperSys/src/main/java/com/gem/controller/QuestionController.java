package com.gem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gem.entity.Question;
import com.gem.entity.QuestionChoice;
import com.gem.service.QuestionChoiceService;
import com.gem.service.QuestionService;
import com.gem.utils.RedisService;
import com.gem.utils.StringUtil;
import com.gem.utils.UUIDUtil;
import com.gem.vo.QuestionVo;
import com.gem.vo.TableVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 2536320610@qq.com
 * @since 2021-01-21
 */
@RestController
@CrossOrigin  //解决跨域问题
@RequestMapping("/question")
public class QuestionController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuestionChoiceService questionChoiceService;
	
	@Autowired
	private RedisService redisService;
	
	
	/**
	 * 查询题目 根据题目设置的过滤条件进行分页
	 *
	 * @param questionVo
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/queryListByPage")
	public TableVo< QuestionVo > queryListByPage(QuestionVo questionVo , String userId , int page , int pageSize) {
		if ( null == userId || !userId.equals(redisService.get(userId)) ) {
			return new TableVo< QuestionVo >(null , -1L);
		}
		if ( questionVo == null ) {
			return null;
		}
		if ( "choice".equals(questionVo.getQueType()) ) {
			return questionChoiceService.queryListByPage(QuestionVo.getQuestionChoice(questionVo) , page , pageSize);
		} else {
			return questionService.queryListByPage(QuestionVo.getQuestion(questionVo) , page , pageSize);
		}
	}
	
	/**
	 * 添加题目
	 *
	 * @param questionVo
	 * @return
	 */
	@PostMapping("/add")
	public Map< String, Object > addQuestionVo(@RequestBody QuestionVo questionVo) {
		HashMap< String, Object > map = new HashMap< String, Object >();
		if ( questionVo == null ) {
			map.put("success" , false);
			map.put("msg" , "数据丢失,添加失败!");
			return map;
		}
		if ( StringUtil.isEmpty(questionVo.getQueAnswerDescribe()) ){
			questionVo.setQueAnswerDescribe("暂无解析");
		}
		if ( "choice".equals(questionVo.getQueType()) ) {
			QueryWrapper< QuestionChoice > questionChoiceQueryWrapper = new QueryWrapper<>();
			questionChoiceQueryWrapper.eq("que_content" , questionVo.getQueContent());
			if ( questionChoiceService.count(questionChoiceQueryWrapper) > 0 ) {
				map.put("success" , false);
				map.put("msg" , "当前题目已存在!!");
				return map;
			}
			questionVo.setQueId(UUIDUtil.get());
			map.put("success" , questionChoiceService.save(QuestionVo.getQuestionChoice(questionVo)));
		} else {
			QueryWrapper< Question > questionQueryWrapper = new QueryWrapper<>();
			questionQueryWrapper.eq("que_content" , questionVo.getQueContent());
			if ( questionService.count(questionQueryWrapper) > 0 ) {
				map.put("success" , false);
				map.put("msg" , "当前题目已存在!!");
				return map;
			}
			questionVo.setQueId(UUIDUtil.get());
			map.put("success" , questionService.save(QuestionVo.getQuestion(questionVo)));
		}
		return map;
		
	}
	
	/**
	 * 删除题目 和 批量删除
	 *
	 * @param queId
	 * @param queType
	 * @return
	 */
	@PostMapping("/del")
	public Map< String, Object > delQuestionByPk(@RequestParam String queId , String queType) {
		HashMap< String, Object > map = new HashMap<>();
		if ( StringUtil.isEmpty(queId) ) {
			map.put("msg" , "数据丢失!!");
			map.put("success" , false);
			return map;
		}
		boolean remove;
		if ( "choice".equals(queType) ) {
			QueryWrapper< QuestionChoice > questionChoiceQueryWrapper = new QueryWrapper<>();
			Object[] ids = queId.split(",");
			questionChoiceQueryWrapper.in("que_id" , ids);
			remove = questionChoiceService.remove(questionChoiceQueryWrapper);
		} else {
			QueryWrapper< Question > questionQueryWrapper = new QueryWrapper<>();
			Object[] ids = queId.split(",");
			questionQueryWrapper.in("que_id" , ids);
			remove = questionService.remove(questionQueryWrapper);
		}
		if ( !remove ) {
			map.put("msg" , "未知错误原因");
		}
		map.put("success" , remove);
		return map;
	}
	
	/**
	 * 编辑题目信息
	 *
	 * @param questionVo
	 * @param queType
	 * @return
	 */
	@PostMapping("/edit")
	public Map< String, Object > editQuestion(@RequestBody QuestionVo questionVo , String queType) {
		HashMap< String, Object > map = new HashMap<>();
		if ( StringUtil.isEmpty(queType) ) {
			queType = questionVo.getQueType();
		}
		if ( null == questionVo || null == queType ) {
			map.put("msg" , "数据丢失");
			map.put("success" , false);
			return map;
		}
		if ( StringUtil.isEmpty(questionVo.getQueAnswerDescribe()) ){
			questionVo.setQueAnswerDescribe("暂无解析");
		}
		boolean update;
		if ( "choice".equals(queType) ) {
//			UpdateWrapper< QuestionChoice > questionChoiceUpdateWrapper = new UpdateWrapper<>();
//			questionChoiceUpdateWrapper.eq("que_id" , questionVo.getQueId());
			QuestionChoice questionChoice = QuestionVo.getQuestionChoice(questionVo);
			System.err.println(questionChoice);
//			update = questionChoiceService.update( questionChoice, questionChoiceUpdateWrapper);
			update = questionChoiceService.updateById(questionChoice);
		} else {
			UpdateWrapper< Question > questionUpdateWrapper = new UpdateWrapper<>();
			questionUpdateWrapper.eq("que_id" , questionVo.getQueId());
			update = questionService.update(QuestionVo.getQuestion(questionVo) , questionUpdateWrapper);
		}
		if ( !update ) {
			map.put("msg" , "未知错误原因!!");
		}
		map.put("success" , update);
		return map;
	}
	
	/**
	 * 获取课程列表
	 *
	 * @return
	 */
	@RequestMapping("/getCourse")
	public void getCourse(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/course/getCourse").forward(request , response);
	}
	
	/**
	 * 获取课程题库
	 *
	 * @return
	 */
	@RequestMapping("/getQueBank")
	public void getQueBank(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/questionBank/getQueBank").forward(request , response);
	}
	
	@RequestMapping("/UntieQuestion")
	public Map< String, Object > UntieQuestion(String queBankId) {
		HashMap< String, Object > map = new HashMap<>();
		if ( StringUtil.isEmpty(queBankId) ) {
			map.put("success" , false);
			map.put("msg" , "数据丢失,请刷新重试!");
			return map;
		}
		/**
		 * 更新判断题和简答题
		 */
		UpdateWrapper< Question > questionUpdateWrapper = new UpdateWrapper<>();
		questionUpdateWrapper.eq("que_bank_id" , queBankId);
		questionUpdateWrapper.set("que_bank_id" , "");
		questionService.update(questionUpdateWrapper);
		
		/**
		 * 更新选择题
		 */
		UpdateWrapper< QuestionChoice > questionChoiceUpdateWrapper = new UpdateWrapper<>();
		questionChoiceUpdateWrapper.eq("que_bank_id" , queBankId);
		questionChoiceUpdateWrapper.set("que_bank_id" , "");
		questionChoiceService.update(questionChoiceUpdateWrapper);
		map.put("success" , true);
		return map;
	}
	
	/**
	 * 转发/knowledge/getKnowledge
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/getKnowledge")
	public void getKnowledge(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/knowledge/getKnowledge").forward(request , response);
	}
	
	/**
	 * 通过ids 获取题目列表
	 *
	 * @param queIds
	 * @return
	 */
	@RequestMapping("/getQueByIds")
	public Map< String, Object > getQueByIds(@RequestBody String queIds) {
		HashMap< String, Object > map = new HashMap<>();
		if ( StringUtil.isEmpty(queIds) ) {
			map.put("msg" , "无法获取试卷,请刷新重试!");
			map.put("success" , false);
			return map;
		}
		Object[] ids = StringUtil.arrStrToArr(queIds);
		QueryWrapper< QuestionChoice > questionChoiceQueryWrapper = new QueryWrapper<>();
		questionChoiceQueryWrapper.in("que_id" , ids);
		List< QuestionChoice > choiceList = questionChoiceService.list(questionChoiceQueryWrapper);
		QueryWrapper< Question > questionQueryWrapper = new QueryWrapper<>();
		questionQueryWrapper.in("que_id" , ids);
		List< Question > questions = questionService.list(questionQueryWrapper);
		List< Question > boolList = new ArrayList<>();
		List< Question > simpleList = new ArrayList<>();
		questions.forEach(que -> {
			if ( que.getQueType().equals("bool") ) {
				boolList.add(que);
			} else {
				simpleList.add(que);
			}
		});
		map.put("success" , true);
		map.put("boolList" , boolList);
		map.put("choiceList" , choiceList);
		map.put("simpleList" , simpleList);
		return map;
	}
	
	/**
	 * 获取总题目的数量
	 * @return
	 */
	@RequestMapping("getQueNo")
	public int getQueNo(){
		return questionService.count()+questionChoiceService.count();
	}
	
}

