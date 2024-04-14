package com.gem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gem.entity.QuestionBank;
import com.gem.service.QuestionBankService;
import com.gem.utils.RedisService;
import com.gem.utils.StringUtil;
import com.gem.utils.UUIDUtil;
import com.gem.vo.QuestionBankVo;
import com.gem.vo.TableVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
@RequestMapping("/questionBank")
public class QuestionBankController {
	
	@Autowired
	private QuestionBankService questionBankService;
	@Autowired
	private RedisService redisService;
	
	/**
	 * 查询课程 根据课程设置的过滤条件进行分页
	 *
	 * @param questionBank
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/queryListByPage")
	public TableVo< QuestionBankVo > queryListByPage(QuestionBank questionBank , String userId , int page , int pageSize) {
		if ( userId == null || !userId.equals(redisService.get(userId)) ) {
			return new TableVo< QuestionBankVo >(null , -1L);
		}
		if ( questionBank == null ) {
			return null;
		}
		return questionBankService.queryListByPage(questionBank , page , pageSize);
	}
	
	/**
	 * 添加课程
	 *
	 * @param  questionBankVo
	 * @return
	 */
	@PostMapping("/add")
	public Map< String, Object > addQueBank(@RequestBody QuestionBankVo questionBankVo) {
		QuestionBank questionBank = QuestionBankVo.getQueBank(questionBankVo);
		HashMap< String, Object > map = new HashMap<>();
		if ( StringUtil.isEmpty(questionBankVo)) {
			map.put("success" , false);
			map.put("msg" , "数据丢失,添加失败");
			return map;
		}
		if ( questionBank.getQueBankName() != null && "".equals(questionBank.getQueBankName()) ) {
			QueryWrapper< QuestionBank > questionBankQueryWrapper = new QueryWrapper<>();
			questionBankQueryWrapper.eq("que_bank_name" , questionBank.getQueBankName());
			if ( questionBankService.count(questionBankQueryWrapper) > 1 ) {
				map.put("success" , false);
				map.put("msg" , "该题库已存在,无法添加!");
				return map;
			}
		}
		questionBank.setQueBankId(UUIDUtil.get());
		map.put("success" , questionBankService.save(questionBank));
		map.put("queBankId" , questionBank.getQueBankId());
		return map;
	}
	
	/**
	 * 删除课程 和 批量删除
	 *
	 * @param queBankId
	 * @return
	 */
	@PostMapping("/del")
	public boolean delQueBankByPk(String queBankId) {
		if ( null == queBankId ) {
			return false;
		}
		QueryWrapper< QuestionBank > questionBankQueryWrapper = new QueryWrapper<>();
		Object[] ids = queBankId.split(",");
		questionBankQueryWrapper.in("que_bank_id" , ids);
		
		//删除题目
		//to do
		return questionBankService.remove(questionBankQueryWrapper);
	}
	
	@PostMapping("/edit")
	public Map< String, Object > editQueBank(@RequestBody QuestionBankVo questionBankVo) {
		QuestionBank questionBank = QuestionBankVo.getQueBank(questionBankVo);
		HashMap< String, Object > map = new HashMap<>();
		if ( null == questionBank.getQueBankId() ) {
			map.put("success" , false);
			map.put("msg" , "数据丢失,无法添加!");
			return map;
		}
		questionBank.setCreateBy(null);
		if ( "T".equals(questionBankVo.getIsChangeQueBankName()) ) {
			QueryWrapper< QuestionBank > queBankQueryWrapper = new QueryWrapper<>();
			queBankQueryWrapper.eq("que_bank_name" , questionBank.getQueBankName());
			if ( questionBankService.count(queBankQueryWrapper) > 0 ) {
				map.put("success" , false);
				map.put("msg" , "该题库已存在,无法修改!");
				return map;
			}
		}
		UpdateWrapper< QuestionBank > queBankUpdateWrapper = new UpdateWrapper<>();
		queBankUpdateWrapper.eq("que_bank_id" , questionBank.getQueBankId());
		map.put("success" , questionBankService.update(questionBank , queBankUpdateWrapper));
		return map;
	}
	
	/**
	 * 根据课程id获取题库
	 *
	 * @param courseId
	 * @return
	 */
	@RequestMapping("/getQueBank")
	public List< QuestionBankVo > getQueBankVo(String courseId) {
		if ( courseId == null ) {
			return null;
		}
		QueryWrapper< QuestionBank > questionBankQueryWrapper = new QueryWrapper<>();
		questionBankQueryWrapper.select("que_bank_id" , "que_bank_name");
		questionBankQueryWrapper.eq("que_bank_course_id" , courseId);
		return QuestionBankVo.getQueBankVoList(questionBankService.list(questionBankQueryWrapper));
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
	
	@RequestMapping("/UntieQuestion")
	public void UntieQuestion(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/question/UntieQuestion").forward(request , response);
	}
	
}

