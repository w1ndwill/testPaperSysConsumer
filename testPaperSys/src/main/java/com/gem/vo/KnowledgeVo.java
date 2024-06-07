package com.gem.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gem.entity.Knowledge;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "KnowledgeVo对象", description = "")
@NoArgsConstructor
public class KnowledgeVo {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "课程知识点id")
	@TableId(value = "knowledge_id", type = IdType.UUID)
	private String knowledgeId;
	
	@ApiModelProperty(value = "课程id")
	private String courseId;
	
	@ApiModelProperty(value = "知识点名字")
	private String knowledgeName;
	
	@ApiModelProperty(value = "知识点描述")
	private String knowledgeDescribe;
	
	private String isChangeKnowledgeName;
	
	
	public KnowledgeVo(Knowledge knowledge) {
		this.courseId = knowledge.getCourseId();
		this.knowledgeId = knowledge.getKnowledgeId();
		this.knowledgeName = knowledge.getKnowledgeName();
		this.knowledgeDescribe = knowledge.getKnowledgeDescribe();
	}
	
	public static Knowledge getKnowledge(KnowledgeVo knowledgeVo) {
		Knowledge knowledge = new Knowledge();
		knowledge.setCourseId(knowledgeVo.getCourseId());
		knowledge.setKnowledgeId(knowledgeVo.getKnowledgeId());
		knowledge.setKnowledgeName(knowledgeVo.getKnowledgeName());
		knowledge.setKnowledgeDescribe(knowledgeVo.getKnowledgeDescribe());
		return knowledge;
	}
//	public static Knowledge getKnowledge(KnowledgeVo knowledgeVo) {
//		Knowledge knowledge = new Knowledge();
//		knowledge.setCourseId(knowledge.getCourseId());
//		knowledge.setKnowledgeId(knowledgeVo.getKnowledgeId());
//		knowledge.setKnowledgeName(knowledgeVo.getKnowledgeName());
//		knowledge.setKnowledgeDescribe(knowledgeVo.getKnowledgeDescribe());
//		return knowledge;
//	}
	
	public static List< KnowledgeVo > getKnowledgeVoList(List< Knowledge > knowledgeList) {
		LinkedList< KnowledgeVo > list = new LinkedList<>();
		for ( Knowledge knowledge : knowledgeList ) {
			list.add(new KnowledgeVo(knowledge));
		}
		return list;
	}
}
