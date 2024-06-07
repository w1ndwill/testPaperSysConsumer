package com.gem.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gem.entity.Question;
import com.gem.entity.QuestionChoice;
import com.gem.utils.ServiceUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "QuestionVo对象", description = "")
@NoArgsConstructor
public class QuestionVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "试题id")
	@TableId(value = "que_id" /*, type = IdType.UUID*/)
	private String queId;
	
	@ApiModelProperty(value = "所属的课程id")
	private String queCourseId;
	
	private String queCourseName;
	
	@ApiModelProperty(value = "所属的题库id")
	private String queBankId;
	
	private String queBankName;
	
	@ApiModelProperty(value = "创建人--跟user_id就名字不一样")
	private String queCreateBy;
	
	private String queCreateByName;
	
	@ApiModelProperty(value = "知识点列表 [1,2,3]")
	private String knowledgeIds;
	
	private String knowledgeNames;
	
	@ApiModelProperty(value = "试题内容")
	private String queContent;
	
	@ApiModelProperty(value = "题型,判断题题(bool),简答题(simple),选择题(choice)")
	private String queType;
	
	@ApiModelProperty(value = "选项A")
	private String queA;
	
	@ApiModelProperty(value = "选项B")
	private String queB;
	
	@ApiModelProperty(value = "选项C")
	private String queC;
	
	@ApiModelProperty(value = "选项D")
	private String queD;
	
	@ApiModelProperty(value = "参考答案---")
	private String queAnswer;
	
	@ApiModelProperty(value = "答案解析")
	private String queAnswerDescribe;
	
	@ApiModelProperty(value = "是否改变 T-改变;F不改变;当前变量只用于接收前端的值")
	private String isChangeQueName;
	
	/**
	 * 题目难度(1-10)
	 */
	private Integer queDifficulty;
	
	
	public QuestionVo(QuestionChoice questionChoice) {
		this.queId = questionChoice.getQueId();
		this.queCourseId = questionChoice.getQueCourseId();
		this.queBankId = questionChoice.getQueBankId();
		this.queCreateBy = questionChoice.getQueCreateBy();
		this.knowledgeIds = questionChoice.getKnowledgeIds();
		// 从数据库中获取对应的名字
		String queCourseName = ServiceUtil.getQueCourseName(questionChoice.getQueCourseId());
		this.queCourseName = queCourseName.length() > 0 ? queCourseName : "暂无";
		String queBankName = ServiceUtil.getQueBankName(questionChoice.getQueBankId());
		this.queBankName = queBankName.length() > 0 ? queBankName : "暂无";
		this.queCreateByName = ServiceUtil.getUserName(questionChoice.getQueCreateBy());
		String knowledgeName = ServiceUtil.getKnowledgeName(questionChoice.getKnowledgeIds());
		this.knowledgeNames = knowledgeName.length() > 0 ? knowledgeName : "暂无";
		//
		this.queContent = questionChoice.getQueContent();
		this.queType = "choice";
		this.queA = questionChoice.getQueA();
		this.queB = questionChoice.getQueB();
		this.queC = questionChoice.getQueC();
		this.queD = questionChoice.getQueD();
		this.queAnswer = questionChoice.getQueAnswer();
		this.queAnswerDescribe = questionChoice.getQueAnswerDescribe();
		this.queDifficulty = questionChoice.getQueDifficulty();
	}
	
	public QuestionVo(Question question) {
		this.queId = question.getQueId();
		this.queCourseId = question.getQueCourseId();
		this.queBankId = question.getQueBankId();
		this.queCreateBy = question.getQueCreateBy();
		this.knowledgeIds =question.getKnowledgeIds();
		// 从数据库中获取对应的名字
		String queCourseName = ServiceUtil.getQueCourseName(question.getQueCourseId());
		this.queCourseName = queCourseName.length() > 0 ? queCourseName : "暂无";
		String queBankName = ServiceUtil.getQueBankName(question.getQueBankId());
		this.queBankName = queBankName.length() > 0 ? queBankName : "暂无";
		this.queCreateByName = ServiceUtil.getUserName(question.getQueCreateBy());
		String knowledgeName = ServiceUtil.getKnowledgeName(question.getKnowledgeIds());
		this.knowledgeNames = knowledgeName.length() > 0 ? knowledgeName : "暂无";
		//
		this.queContent = question.getQueContent();
		this.queType = question.getQueType();
		this.queAnswer = question.getQueAnswer();
		this.queAnswerDescribe = question.getQueAnswerDescribe();
		this.queDifficulty = question.getQueDifficulty();
	}
	
	
	public static List< QuestionVo > getQuestionVoList(List< QuestionChoice > questionChoiceList) {
		LinkedList< QuestionVo > questionVos = new LinkedList<>();
		for ( QuestionChoice questionChoice : questionChoiceList ) {
			questionVos.add(new QuestionVo(questionChoice));
		}
		return questionVos;
	}
	
	/**
	 * @param questionList
	 * @param d            该参数没有用,为了解决冲突加上的.
	 * @return
	 */
	public static List< QuestionVo > getQuestionVoList(List< Question > questionList , String... d) {
		LinkedList< QuestionVo > questionVos = new LinkedList<>();
		for ( Question question : questionList ) {
			questionVos.add(new QuestionVo(question));
		}
		return questionVos;
	}
	
	public static Question getQuestion(QuestionVo questionVo) {
		Question question = new Question();
		question.setQueId(questionVo.getQueId());
		question.setQueCourseId(questionVo.getQueCourseId());
		question.setQueBankId(questionVo.getQueBankId());
		question.setKnowledgeIds(questionVo.getKnowledgeIds());
		question.setQueCreateBy(questionVo.getQueCreateBy());
		question.setQueContent(questionVo.getQueContent());
		question.setQueType(questionVo.getQueType());
		question.setQueAnswer(questionVo.getQueAnswer());
		question.setQueAnswerDescribe(questionVo.getQueAnswerDescribe());
		question.setQueDifficulty(questionVo.getQueDifficulty());
		return question;
	}
	
	public static QuestionChoice getQuestionChoice(QuestionVo questionVo) {
		QuestionChoice questionChoice = new QuestionChoice();
		questionChoice.setQueId(questionVo.getQueId());
		questionChoice.setQueCourseId(questionVo.getQueCourseId());
		questionChoice.setQueBankId(questionVo.getQueBankId());
		questionChoice.setQueCreateBy(questionVo.getQueCreateBy());
		questionChoice.setQueContent(questionVo.getQueContent());
		questionChoice.setKnowledgeIds(questionVo.getKnowledgeIds());
		questionChoice.setQueA(questionVo.getQueA());
		questionChoice.setQueB(questionVo.getQueB());
		questionChoice.setQueC(questionVo.getQueC());
		questionChoice.setQueD(questionVo.getQueD());
		questionChoice.setQueAnswer(questionVo.getQueAnswer());
		questionChoice.setQueAnswerDescribe(questionVo.getQueAnswerDescribe());
		questionChoice.setQueDifficulty(questionVo.getQueDifficulty());
		return questionChoice;
	}
}
