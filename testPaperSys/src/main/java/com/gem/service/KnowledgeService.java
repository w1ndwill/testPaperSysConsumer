package com.gem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.entity.Knowledge;
import com.gem.vo.KnowledgeVo;
import com.gem.vo.TableVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 */
public interface KnowledgeService extends IService< Knowledge > {
	/**
	 * 根据 知识点名进行模糊查询 并且分页
	 *
	 * @param knowledgeVo
	 * @param page
	 * @param pageSize
	 * @return
	 */
	TableVo< KnowledgeVo > queryListByPage(KnowledgeVo knowledgeVo , int page , int pageSize);
	
	/**
	 * 通过 知识点id列表 获取 知识点名字列表
	 * @param knowledgeIds
	 * @return
	 */
	String getKnowledgeNames(String knowledgeIds);
}
