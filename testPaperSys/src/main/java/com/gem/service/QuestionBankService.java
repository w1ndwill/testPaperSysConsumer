package com.gem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.entity.QuestionBank;
import com.gem.vo.QuestionBankVo;
import com.gem.vo.TableVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 */
public interface QuestionBankService extends IService< QuestionBank > {
	
	TableVo< QuestionBankVo > queryListByPage(QuestionBank questionBank , int page , int pageSize);
	/**
	 * 根据题库 id获取题库名
	 * @param queBankId
	 * @return
	 */
	String getQueBankName(String queBankId);
}
