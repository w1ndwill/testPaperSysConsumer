package com.gem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.entity.QuestionChoice;
import com.gem.mapper.QuestionChoiceMapper;
import com.gem.service.QuestionChoiceService;
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
 * @author 2536320610@qq.com
 * @since 2021-01-21
 */
@Service
public class QuestionChoiceServiceImpl extends ServiceImpl< QuestionChoiceMapper, QuestionChoice > implements QuestionChoiceService {
	@Autowired(required = false)
	private QuestionChoiceMapper questionChoiceMapper;
	
	@Override
	public TableVo< QuestionVo > queryListByPage(QuestionChoice questionChoice , int page , int pageSize) {
		Page< QuestionChoice > questionChoicePage = new Page<>(page , pageSize);
		QueryWrapper< QuestionChoice > questionChoiceQueryWrapper = new QueryWrapper<>();
		if ( questionChoice.getQueBankId().length() > 0 ) {
			questionChoiceQueryWrapper.eq("que_bank_id" , questionChoice.getQueBankId());
		}
		if ( questionChoice.getQueCourseId().length() > 0 ) {
			questionChoiceQueryWrapper.eq("que_course_id" , questionChoice.getQueCourseId());
		}
//		System.err.println(questionChoice.getQueCreateBy());
		if ( questionChoice.getQueCreateBy().length() > 0 ) {
			questionChoiceQueryWrapper.eq("que_create_by" , questionChoice.getQueCreateBy());
		}
		
		if ( questionChoice.getQueContent().length() > 0 ) {
			StringBuffer str = new StringBuffer("%");
			for ( int i = 0; i < questionChoice.getQueContent().length(); i++ ) {
				str.append(questionChoice.getQueContent().charAt(i)).append("%");
			}
			questionChoiceQueryWrapper.like("que_content" , str);
		}
		questionChoiceQueryWrapper.orderByDesc("create_time");
		Page< QuestionChoice > resultPage = questionChoiceMapper.selectPage(questionChoicePage , questionChoiceQueryWrapper);
		return new TableVo< QuestionVo >(QuestionVo.getQuestionVoList(resultPage.getRecords()) , resultPage.getTotal());
	}
	
	@Override
	public List< QuestionChoice > listByRand(String tpCourseId,Integer choiceNo) {
		return questionChoiceMapper.listByRand(tpCourseId,choiceNo);
	}
	
	
}
