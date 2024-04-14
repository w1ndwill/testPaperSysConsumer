package com.gem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gem.entity.Knowledge;
import com.gem.service.KnowledgeService;
import com.gem.utils.RedisService;
import com.gem.utils.StringUtil;
import com.gem.utils.UUIDUtil;
import com.gem.vo.KnowledgeVo;
import com.gem.vo.TableVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 */
@RestController
@CrossOrigin  //解决跨域问题
@RequestMapping("/knowledge")
public class KnowledgeController {
	
	@Autowired
	private KnowledgeService knowledgeService;
	
	@Autowired
	private RedisService redisService;
	
	/**
	 * 查询知识点 根据知识点设置的过滤条件进行分页
	 *
	 * @param knowledgeVo
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/queryListByPage")
	public TableVo< KnowledgeVo > queryListByPage(KnowledgeVo knowledgeVo , String userId , int page , int pageSize) {
		if ( userId == null || !userId.equals(redisService.get(userId)) ) {
			return new TableVo< KnowledgeVo >(null , -1L);
		}
		if ( knowledgeVo == null || knowledgeVo.getCourseId().length() <= 0 ) {
			return null;
		}
		return knowledgeService.queryListByPage(knowledgeVo , page , pageSize);
	}
	
	/**
	 * 添加知识点
	 *
	 * @param knowledgeVo
	 * @return
	 */
	@PostMapping("/add")
	public Map< String, Object > addKnowledgeVo(@RequestBody KnowledgeVo knowledgeVo) {
		System.out.println(knowledgeVo);
		HashMap< String, Object > map = new HashMap<>();
		if ( knowledgeVo == null ) {
			map.put("success" , false);
			map.put("msg" , "数据丢失,添加失败");
			return map;
		}
		Knowledge knowledge = KnowledgeVo.getKnowledge(knowledgeVo);
		QueryWrapper< Knowledge > knowledgeQueryWrapper = new QueryWrapper<>();
		knowledgeQueryWrapper.eq("knowledge_name" , knowledge.getKnowledgeName());
		if ( knowledgeService.count(knowledgeQueryWrapper) > 0 ) {
			map.put("success" , false);
			map.put("msg" , "该题目已存在,无法添加!");
			return map;
		}
		knowledge.setKnowledgeId(UUIDUtil.get());
		map.put("success" , knowledgeService.save(knowledge));
		map.put("knowledgeId" , knowledge.getKnowledgeId());
		return map;
	}
	
	/**
	 * 删除知识点 和 批量删除
	 *
	 * @param knowledgeId
	 * @return
	 */
	@PostMapping("/del")
	public boolean deKnowledgeByPk(String knowledgeId) {
		if ( null == knowledgeId ) {
			return false;
		}
		QueryWrapper< Knowledge > knowledgeQueryWrapper = new QueryWrapper<>();
		Object[] ids = knowledgeId.split(",");
		knowledgeQueryWrapper.in("knowledge_id" , ids);
		
		return knowledgeService.remove(knowledgeQueryWrapper);
	}
	
	/**
	 * @param knowledgeVo
	 * @return
	 */
	@PostMapping("/edit")
	public Map< String, Object > editKnowledge(@RequestBody KnowledgeVo knowledgeVo) {
		Knowledge knowledge = KnowledgeVo.getKnowledge(knowledgeVo);
		HashMap< String, Object > map = new HashMap<>();
		if ( null == knowledge.getKnowledgeId() ) {
			map.put("success" , false);
			map.put("msg" , "数据丢失,无法添加!");
			return map;
		}
		QueryWrapper< Knowledge > knowledgeQueryWrapper = new QueryWrapper<>();
		knowledgeQueryWrapper.eq("knowledge_name" , knowledge.getKnowledgeName());
		if ( "T".equals(knowledgeVo.getIsChangeKnowledgeName()) ) {
			if ( knowledgeService.count(knowledgeQueryWrapper) > 0 ) {
				map.put("success" , false);
				map.put("msg" , "该知识点已存在,无法修改!");
				return map;
			}
		} else {
			knowledge.setKnowledgeName(null);
		}
		
		UpdateWrapper< Knowledge > knowledgeUpdateWrapper = new UpdateWrapper<>();
		knowledgeUpdateWrapper.eq("knowledge_id" , knowledge.getKnowledgeId());
		map.put("success" , knowledgeService.update(knowledge , knowledgeUpdateWrapper));
		return map;
	}
	
	/**
	 * 获取知识点列表
	 *
	 * @param courseId
	 * @return
	 */
	@RequestMapping("/getKnowledge")
	public Map< String, Object > getKnowledge(String courseId) {
		HashMap< String, Object > map = new HashMap<>();
		if ( StringUtil.isEmpty(courseId)) {
			map.put("msg" , "数据丢失!!,请刷新重试!!");
			map.put("success" , false);
			return map;
		}
		QueryWrapper< Knowledge > knowledgeQueryWrapper = new QueryWrapper<>();
		knowledgeQueryWrapper.eq("course_id" , courseId);
		knowledgeQueryWrapper.select("knowledge_id" , "knowledge_name");
		List< KnowledgeVo > knowledgeVoList = KnowledgeVo.getKnowledgeVoList(knowledgeService.list(knowledgeQueryWrapper));
		map.put("success" , true);
		map.put("data" , knowledgeVoList);
		return map;
	}
	
	
}
