package com.gem.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gem.entity.QuestionBank;
import com.gem.utils.ServiceUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 2536320610@qq.com
 * @since 2021-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "QuestionBankVo对象", description = "")
@ToString
@NoArgsConstructor
public class QuestionBankVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@ApiModelProperty(value = "题库id")
	@TableId(value = "que_bank_id", type = IdType.UUID)
	private String queBankId;
	
	@ApiModelProperty(value = "题库名字")
	private String queBankName;
	
	@ApiModelProperty(value = "题库描述")
	private String queBankDescribe;
	
	@ApiModelProperty(value = "所属课程名...")
	private String courseName;
	
	private String courseId;
	
	@ApiModelProperty(value = "课程id,--外键")
	private String isChangeQueBankName;
	
	/**
	 * 创建人
	 */
	private String createBy;
	
	public QuestionBankVo(QuestionBank questionBank) {
		this.queBankId = questionBank.getQueBankId();
		this.queBankName = questionBank.getQueBankName();
		this.queBankDescribe = questionBank.getQueBankDescribe();
		if ( null == questionBank.getQueBankCourseId() || "".equals(questionBank.getQueBankCourseId()) ) {
			this.courseName = "暂无";
		} else {
			this.courseName = new ServiceUtil().getCourseName(questionBank.getQueBankCourseId());
		}
		this.createBy = ServiceUtil.getUserName(questionBank.getCreateBy());
	}
	
	public static List< QuestionBankVo > getQueBankVoList(List< QuestionBank > questionBankList) {
		List< QuestionBankVo > list = new LinkedList<>();
		for ( QuestionBank questionBank : questionBankList ) {
			list.add(new QuestionBankVo(questionBank));
		}
		return list;
	}
	
	public static QuestionBank getQueBank(QuestionBankVo questionBankVo) {
		QuestionBank questionBank = new QuestionBank();
		questionBank.setQueBankId(questionBankVo.getQueBankId());
		questionBank.setQueBankCourseId(questionBankVo.getCourseName());
		questionBank.setQueBankName(questionBankVo.getQueBankName());
		questionBank.setQueBankDescribe(questionBankVo.getQueBankDescribe());
		questionBank.setQueBankCourseId(questionBankVo.getCourseId());
		questionBank.setCreateBy(questionBankVo.getCreateBy());
		return questionBank;
	}
}
