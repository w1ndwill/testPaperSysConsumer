package com.gem.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gem.entity.Knowledge;
import com.gem.entity.TestPaper;
import com.gem.utils.ServiceUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Date : 2021/2/6 16:15
 * package: com.gem.vo
 * ClassName : TestPaperVo
 * 描述:
 **/
@Data
@NoArgsConstructor
@ToString
public class TestPaperVo {
	private String isChangeTpName;
	
	/**
	 * 试卷id
	 */
	@TableId(value = "tp_id"/*, type = IdType.UUID*/)
	private String tpId;
	
	/**
	 * 试卷名字
	 */
	private String tpName;
	
	/**
	 * 创建人
	 */
	private String tpCreateBy;
	
	private String tpCreateByName;
	
	/**
	 * 所属的课程id
	 */
	private String tpCourseId;
	
	/**
	 * 试卷名(前端使用字段)
	 */
	private String tpCourseName;
	
	/**
	 * 试题的id列表
	 */
	private String tpQueIds;
	
	/**
	 * 知识点id列表
	 */
	private String tpKnowledgeIds;
	
	/**
	 * 知识点名字列表(前端使用)
	 */
	private String tpKnowledgeNames;
	/**
	 * 知识点列表(前端使用)
	 */
	private List< Knowledge > knowledgeList;
	
	/**
	 * 试卷描述
	 */
	private String tpDescribe;
	
	// 以下是前端使用字段
	/**
	 * 自动添加时的判断题数量
	 */
	private Integer boolNo;
	/**
	 * 自动添加时选择题数量
	 */
	private Integer choiceNo;
	/**
	 * 自动添加时简答题数量
	 */
	private Integer simpleNo;
	/**
	 * 是否是自动添加?
	 */
	private Boolean isAutoAdd;
	
	
	
	public static TestPaper getTestPaper(TestPaperVo testPaperVo) {
		TestPaper testPaper = new TestPaper();
		testPaper.setTpId(testPaperVo.getTpId());
		testPaper.setTpName(testPaperVo.getTpName());
		testPaper.setTpCreateBy(testPaperVo.getTpCreateBy());
		testPaper.setTpCourseId(testPaperVo.getTpCourseId());
		testPaper.setTpKnowledgeIds(testPaperVo.getTpKnowledgeIds());
		testPaper.setTpDescribe(testPaperVo.getTpDescribe());
		testPaper.setTpQueIds(testPaperVo.getTpQueIds());
		return testPaper;
	}
	
    public 	TestPaperVo(TestPaper testPaper){
		this.tpId = testPaper.getTpId();
		this.tpName = testPaper.getTpName();
		this.tpCourseName = ServiceUtil.getQueCourseName(testPaper.getTpCourseId());
		this.tpCourseId = testPaper.getTpCourseId();
		this.tpCreateByName = ServiceUtil.getUserName(testPaper.getTpCreateBy());
		this.tpDescribe = testPaper.getTpDescribe();
		this.tpKnowledgeNames = ServiceUtil.getKnowledgeName(testPaper.getTpKnowledgeIds());
		this.knowledgeList = ServiceUtil.getKnowledgeByIds(testPaper.getTpKnowledgeIds());
		this.tpQueIds = testPaper.getTpQueIds();
	}
	
	public static List<TestPaperVo> getTestPaperList(List<TestPaper> testPaperList){
		ArrayList< TestPaperVo > list = new ArrayList<>();
		testPaperList.forEach(testPaper -> {
			list.add(new TestPaperVo(testPaper));
		});
		return list;
	}
}
