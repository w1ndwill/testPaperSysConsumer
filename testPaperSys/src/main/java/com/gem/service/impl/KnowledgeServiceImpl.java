package com.gem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.entity.Knowledge;
import com.gem.mapper.KnowledgeMapper;
import com.gem.service.KnowledgeService;
import com.gem.utils.StringUtil;
import com.gem.vo.KnowledgeVo;
import com.gem.vo.TableVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 2536320610@qq.com
 * @since 2021-01-24
 */
@Service
public class KnowledgeServiceImpl extends ServiceImpl< KnowledgeMapper, Knowledge > implements KnowledgeService {
	@Autowired(required = false)
	private KnowledgeMapper knowledgeMapper;
	
	@Override
	public TableVo< KnowledgeVo > queryListByPage(KnowledgeVo knowledgeVo , int page , int pageSize) {
		Page< Knowledge > knowledgePage = new Page< Knowledge >(page , pageSize);
		QueryWrapper< Knowledge > knowledgeQueryWrapper = new QueryWrapper<>();
		if ( knowledgeVo.getKnowledgeName().length() > 0 ) {
			StringBuffer str = new StringBuffer("%");
			for ( int i = 0; i < knowledgeVo.getKnowledgeName().length(); i++ ) {
				str.append(knowledgeVo.getKnowledgeName().charAt(i)).append("%");
			}
			knowledgeQueryWrapper.like("knowledge_name" , str);
		}
		if ( knowledgeVo.getCourseId().length() > 0 ) {
			knowledgeQueryWrapper.eq("course_id" , knowledgeVo.getCourseId());
		}
		knowledgeQueryWrapper.orderByDesc("create_time");
		Page< Knowledge > resultPage = knowledgeMapper.selectPage(knowledgePage , knowledgeQueryWrapper);
		return new TableVo< KnowledgeVo >(KnowledgeVo.getKnowledgeVoList(resultPage.getRecords()) , resultPage.getTotal());
	}
	
	@Override
	public String getKnowledgeNames(String knowledgeIds) {
		if ( StringUtil.isEmpty(knowledgeIds) ) {
			return "暂无";
		}
		QueryWrapper< Knowledge > knowledgeQueryWrapper = new QueryWrapper<>();
		Object[] ids = StringUtil.arrStrToArr(knowledgeIds);
		knowledgeQueryWrapper.in("knowledge_id" , ids);
		knowledgeQueryWrapper.select("knowledge_name");
		List< Knowledge > knowledgeList = knowledgeMapper.selectList(knowledgeQueryWrapper);
		StringBuffer str = new StringBuffer("【");
		for ( Knowledge knowledge : knowledgeList ) {
			str.append(knowledge.getKnowledgeName()).append(",");
		}
		if ( knowledgeList.size() > 0 ) {
			return str.substring(0 , str.length() - 1) + "】";
		}
		return "暂无~";
	}
}
