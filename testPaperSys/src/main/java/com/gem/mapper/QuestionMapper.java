package com.gem.mapper;

import com.gem.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface QuestionMapper extends BaseMapper<Question> {
	/**
	 * 根据所属课程、题型和数量随机获取题目
	 * @param queNo
	 * @param queType
	 * @return
	 */
	@Select("SELECT que_id,knowledge_ids FROM  question WHERE que_type = #{queType} AND que_course_id = #{tpCourseId}  ORDER BY  RAND() LIMIT #{queNo}")
	List< Question> listByRand(String tpCourseId,Integer queNo , String queType);
}
