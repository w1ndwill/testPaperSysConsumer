package com.gem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.entity.Question;
import com.gem.vo.QuestionVo;
import com.gem.vo.TableVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 */
public interface QuestionService extends IService< Question > {
	
	TableVo< QuestionVo > queryListByPage(Question question , int page , int pageSize);
	
	/**
	 * 根据所属课程、题型和数量随机获取题目
	 * @param queNo
	 * @param queType
	 * @return
	 */
	List< Question> listByRand(String tpCourseId,Integer queNo , String queType);
}
