package com.gem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gem.entity.TestPaper;
import com.gem.service.TestPaperService;
import com.gem.utils.PaperUtil;
import com.gem.utils.RedisService;
import com.gem.utils.StringUtil;
import com.gem.utils.UUIDUtil;
import com.gem.vo.TableVo;
import com.gem.vo.TestPaperVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 */
@RestController
@CrossOrigin  //解决跨域问题
@RequestMapping("/testPaper")
public class TestPaperController {
	@Autowired
	private TestPaperService testPaperService;
	
	@Autowired
	private RedisService redisService;
	
	/**
	 * 查询试卷 根据试卷设置的过滤条件进行分页
	 *
	 * @param testPaperVo
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/queryListByPage")
	public TableVo< TestPaperVo > queryListByPage(TestPaperVo testPaperVo , String userId , int page , int pageSize) {
		if ( StringUtil.isEmpty(userId) || !userId.equals(redisService.get(userId)) ) {
			return new TableVo< TestPaperVo >(null , -1L);
		}
		if ( StringUtil.isEmpty(testPaperVo) ) {
			return null;
		}
		return testPaperService.queryListByPage(testPaperVo , page , pageSize);
	}
	
	/**
	 * 添加试卷
	 *
	 * @param testPaperVo
	 * @return
	 */
	@PostMapping("/add")
	public Map< String, Object > addTestPaper(@RequestBody TestPaperVo testPaperVo) {
//		System.out.println(testPaperVo);
		HashMap< String, Object > map = new HashMap<>();
		if ( StringUtil.isEmpty(testPaperVo) ) {
			map.put("success" , false);
			map.put("msg" , "数据丢失,添加失败");
			return map;
		}
		TestPaper testPaper = TestPaperVo.getTestPaper(testPaperVo);

		if (  StringUtil.isEmpty(testPaper.getTpDescribe())){
			Date time = new Date();
			DateFormat df = DateFormat.getDateTimeInstance();
			String formatTime = df.format(time);
			testPaper.setTpDescribe("该试卷是在   "+formatTime+(testPaperVo.getIsAutoAdd()?"  [自动":"  [手动")+"]  生成的试卷");
		}
		QueryWrapper< TestPaper > testPaperQueryWrapper = new QueryWrapper<>();
		testPaperQueryWrapper.eq("tp_name" , testPaper.getTpName());
		if ( testPaperService.count(testPaperQueryWrapper) > 0 ) {
			map.put("success" , false);
			map.put("msg" , "该试卷名已存在,无法添加!");
			return map;
		}
		testPaper.setTpId(UUIDUtil.get());
		if ( !testPaperVo.getIsAutoAdd() ) {
			if ( testPaperVo.getTpQueIds().length()==0 || testPaperVo.getTpKnowledgeIds().length() ==0 ){
				map.put("success" , false);
				map.put("msg" , "数据丢失,添加失败");
				return map;
			}else {
				testPaper.setTpQueIds(testPaperVo.getTpQueIds());
				map.put("msg", "自定义试卷添加成功~~~");
			}
		}else {
			//调用随机生成试卷算法
			Map< String, Object > testPaperMaps = PaperUtil.getRandomTestPaperMaps(testPaperVo);
			if ( testPaperMaps.get("success").equals(false) ) {
				return testPaperMaps;
			}
			testPaper.setTpQueIds(testPaperMaps.get("idList").toString());
			testPaperVo.setTpKnowledgeIds(testPaperMaps.get("knowledgeIds").toString());
			map.put("msg", "自动生成试卷成功~~~");
		}
		/**
		 * 处理知识点
		 */
		String[] ids = StringUtil.arrStrToArr(testPaperVo.getTpKnowledgeIds());
		HashSet< String > set = new HashSet<>();
		ArrayList< String > idList = new ArrayList<>();
		for ( String id : ids ) {
			if ( !set.contains(id) ){
				set.add(id);
				idList.add(id);
			}
		}
		testPaper.setTpKnowledgeIds(idList.toString());
		boolean save = testPaperService.save(testPaper);
		if ( !save ){
			map.put("msg", "未知错误原因~~");
		}
		map.put("success" , save);
		map.put("tpId" , testPaper.getTpId());
		return map;
	}
	
	/**
	 * 删除试卷 和 批量删除
	 *
	 * @param tpId
	 * @return
	 */
	@PostMapping("/del")
	public Map< String, Object > delTestPaperByPk(String tpId) {
		HashMap< String, Object > map = new HashMap<>();
		if ( StringUtil.isEmpty(tpId) ) {
			map.put("msg" , "数据丢失,请刷新重试!!");
			map.put("success" , false);
			return map;
		}
		QueryWrapper< TestPaper > testPaperQueryWrapper = new QueryWrapper<>();
		Object[] ids = tpId.split(",");
		testPaperQueryWrapper.in("tp_id" , ids);
		boolean remove = testPaperService.remove(testPaperQueryWrapper);
		if ( !remove ) {
			map.put("msg" , false);
		}
		map.put("success" , remove);
		return map;
	}
	
	/**
	 * @param testPaperVo
	 * @return
	 */
	@PostMapping("/edit")
	public Map< String, Object > editTestPaper(@RequestBody TestPaperVo testPaperVo) {
		TestPaper testPaper = TestPaperVo.getTestPaper(testPaperVo);
		HashMap< String, Object > map = new HashMap<>();
		if ( StringUtil.isEmpty(testPaper.getTpId())   ) {
			map.put("success" , false);
			map.put("msg" , "数据丢失,无法添加!");
			return map;
		}
		System.err.println(testPaperVo);
		QueryWrapper< TestPaper > testPaperQueryWrapper = new QueryWrapper<>();
		testPaperQueryWrapper.eq("tp_name" , testPaper.getTpName());
		if ( "T".equals(testPaperVo.getIsChangeTpName()) ){
			if ( testPaperService.count(testPaperQueryWrapper) > 0 ) {
				map.put("success" , false);
				map.put("msg" , "该试卷已存在,无法修改!");
				return map;
			}
		} else {
			testPaper.setTpName(null);
		}
		
		UpdateWrapper< TestPaper > testPaperUpdateWrapper = new UpdateWrapper<>();
		testPaperUpdateWrapper.eq("tp_id" , testPaper.getTpId());
		boolean update = testPaperService.update(testPaper , testPaperUpdateWrapper);
		if ( !update ){
			map.put("msg", "未知错误原因");
		}
		map.put("success" , update);
		return map;
	}
	
	/**
	 * 获取课程列表
	 *
	 * @return
	 */
	@RequestMapping("/getCourse")
	public void getCourse(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/course/getCourse").forward(request , response);
	}
	
	
	/**
	 * 通过题目ids获取题目列表
	 *
	 * @return
	 */
	@RequestMapping("/getQueByIds")
	public void getQueByIds(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/question/getQueByIds").forward(request , response);
	}
	
	/**
	 * 获取试卷总数量
	 * @return
	 */
	@RequestMapping("getTpNo")
	public Integer getTpNo(){
		return testPaperService.count();
	}
}

