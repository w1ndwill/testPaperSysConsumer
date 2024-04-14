package com.gem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.entity.QuestionChoice;
import com.gem.vo.QuestionVo;
import com.gem.vo.TableVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 */
public interface QuestionChoiceService extends IService< QuestionChoice > {
	
	TableVo< QuestionVo > queryListByPage(QuestionChoice questionChoice , int page , int pageSize);
	
	/**
	 * 随机获取选择题
	 * @param choiceNo
	 * @return
	 */
	List< QuestionChoice> listByRand(String tpCourseId,Integer choiceNo);
}
