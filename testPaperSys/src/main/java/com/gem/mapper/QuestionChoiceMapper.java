package com.gem.mapper;

import com.gem.entity.QuestionChoice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface QuestionChoiceMapper extends BaseMapper<QuestionChoice> {
	/**
	 * 随机获取选择题
	 * @param choiceNo
	 * @return
	 */
	@Select("SELECT que_id,knowledge_ids FROM  question_choice WHERE que_course_id = #{tpCourseId} ORDER BY  RAND() LIMIT #{choiceNo}")
	List< QuestionChoice> listByRand(String tpCourseId,Integer choiceNo);
}
