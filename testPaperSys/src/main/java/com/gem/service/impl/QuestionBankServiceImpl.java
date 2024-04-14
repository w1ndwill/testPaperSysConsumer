package com.gem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.entity.QuestionBank;
import com.gem.mapper.QuestionBankMapper;
import com.gem.service.QuestionBankService;
import com.gem.vo.QuestionBankVo;
import com.gem.vo.TableVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 2536320610@qq.com
 * @since 2021-01-21
 */
@Service
public class QuestionBankServiceImpl extends ServiceImpl< QuestionBankMapper, QuestionBank > implements QuestionBankService {
	
	@Autowired(required = false)
	private QuestionBankMapper questionBankMapper;
	
	@Override
	public TableVo< QuestionBankVo > queryListByPage(QuestionBank questionBank , int page , int pageSize) {
		Page< QuestionBank > questionBankPage = new Page<>(page , pageSize);
		QueryWrapper< QuestionBank > questionBankQueryWrapper = new QueryWrapper<>();
		if ( questionBank.getQueBankName().length() > 0 ) {
			StringBuffer stringBuffer = new StringBuffer("%");
			for ( int i = 0; i < questionBank.getQueBankName().length(); i++ ) {
				stringBuffer.append(questionBank.getQueBankName().charAt(i)).append("%");
			}
			questionBankQueryWrapper.like("que_bank_name" , stringBuffer);
		}
		if ( questionBank.getQueBankCourseId().length() > 0 ) {
			questionBankQueryWrapper.like("que_bank_course_id" , questionBank.getQueBankCourseId());
		}
		questionBankQueryWrapper.orderByDesc("create_time");
		Page< QuestionBank > resultPage = questionBankMapper.selectPage(questionBankPage , questionBankQueryWrapper);
		return new TableVo< QuestionBankVo >(QuestionBankVo.getQueBankVoList(resultPage.getRecords()) , resultPage.getTotal());
	}
	
	@Override
	public String getQueBankName(String queBankId) {
		QueryWrapper< QuestionBank > questionBankQueryWrapper = new QueryWrapper<>();
		questionBankQueryWrapper.select("que_bank_name");
		questionBankQueryWrapper.eq("que_bank_id" , queBankId);
		QuestionBank questionBank = questionBankMapper.selectOne(questionBankQueryWrapper);
		if ( null == questionBank ) {
			return "暂无";
		}
		return questionBank.getQueBankName();
	}
}

