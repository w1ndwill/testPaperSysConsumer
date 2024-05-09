package com.gem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.entity.Question;
import com.gem.mapper.QuestionMapper;
import com.gem.service.QuestionService;
import com.gem.vo.QuestionVo;
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
public class QuestionServiceImpl extends ServiceImpl< QuestionMapper, Question > implements QuestionService {
	@Autowired(required = false)
	private QuestionMapper questionMapper;
	
	@Override
	public TableVo< QuestionVo > queryListByPage(Question question , int page , int pageSize) {
		Page< Question > questionPage = new Page<>(page , pageSize);
		QueryWrapper< Question > questionQueryWrapper = new QueryWrapper<>();
		if (  question.getQueBankId().length()>0 ) {
			questionQueryWrapper.eq("que_bank_id" , question.getQueBankId());
		}
		if ( question.getQueCourseId().length()>0 ) {
			questionQueryWrapper.eq("que_course_id" , question.getQueCourseId());
		}
		if ( question.getQueCreateBy().length()>0 ) {
			questionQueryWrapper.eq("que_create_by" , question.getQueCreateBy());
		}
		questionQueryWrapper.eq("que_type" , question.getQueType());
		if ( question.getQueContent().length()>0 ) {
			StringBuffer str = new StringBuffer("%");
			for ( int i = 0; i < question.getQueContent().length(); i++ ) {
				str.append(question.getQueContent().charAt(i)).append("%");
			}
			questionQueryWrapper.like("que_content" , str);
		}
		questionQueryWrapper.orderByDesc("create_time");
		Page< Question > resultPage = questionMapper.selectPage(questionPage , questionQueryWrapper);
		return new TableVo< QuestionVo >(QuestionVo.getQuestionVoList(resultPage.getRecords()) , resultPage.getTotal());
	}
	
	@Override
	public List< Question > listByRand(String tpCourseId,Integer queNo , String queType) {
		return questionMapper.listByRand( tpCourseId,queNo,queType);
	}
}
