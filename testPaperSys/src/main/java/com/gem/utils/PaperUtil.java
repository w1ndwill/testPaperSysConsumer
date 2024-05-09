package com.gem.utils;

import com.gem.entity.Question;
import com.gem.entity.QuestionChoice;
import com.gem.vo.TestPaperVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * package: com.gem.utils
 * ClassName : paperUtil
 * 描述: 试卷自动生成 -- 工具类
 **/
@Component
public class PaperUtil {
	/**
	 * 获取随机的题目id
	 * @return  直接返回 map{'success':bool,'idList':[],'knowledgeIds':[]}
	 */
	public static Map<String,Object> getRandomTestPaperMaps(TestPaperVo testPaperVo){
		/*SELECT * FROM  tableNAme  ORDER BY  RAND() LIMIT 10*/
		HashMap< String, Object > map = new HashMap<>();
		if (null == testPaperVo.getChoiceNo() ||null == testPaperVo.getBoolNo() ||null == testPaperVo.getSimpleNo()){
			map.put("msg", "数据丢失,请刷新重试!!");
			map.put("success", false);
			return map;
		}
		ArrayList< String > idList = new ArrayList<>();
		ArrayList<String > knowledgeIds =new ArrayList<>();
		List< QuestionChoice > choiceList = ServiceUtil.getChoiceQueList(testPaperVo.getTpCourseId(),testPaperVo.getChoiceNo());
		List< Question > boolList = ServiceUtil.getQueList(testPaperVo.getTpCourseId(),testPaperVo.getBoolNo(),"bool");
		List<Question> simpleList = ServiceUtil.getQueList(testPaperVo.getTpCourseId(),testPaperVo.getSimpleNo(), "simple");
		int sumNo= testPaperVo.getChoiceNo() + testPaperVo.getBoolNo()+ testPaperVo.getSimpleNo();
		for ( QuestionChoice questionChoice:choiceList ){
			idList.add(questionChoice.getQueId());
			knowledgeIds.add(questionChoice.getKnowledgeIds());
		}
		for ( Question question:boolList ){
			idList.add(question.getQueId());
			knowledgeIds.add(question.getKnowledgeIds());
		}
		for ( Question question:simpleList ){
			idList.add(question.getQueId());
			knowledgeIds.add(question.getKnowledgeIds());
		}
		if ( sumNo!=idList.size() ){
			map.put("success", false);
			System.err.println(sumNo+"--"+idList.size());
			map.put("msg", "该课程题目数量不足!无法自动生成试卷");
			return map;
		}
//		System.out.println(idList.size());
		map.put("knowledgeIds",knowledgeIds);
		map.put("idList", idList);
		map.put("success", true);
		return map;
	}
}
