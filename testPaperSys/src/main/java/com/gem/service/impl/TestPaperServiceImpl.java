package com.gem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.entity.TestPaper;
import com.gem.mapper.TestPaperMapper;
import com.gem.service.TestPaperService;
import com.gem.vo.TableVo;
import com.gem.vo.TestPaperVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class TestPaperServiceImpl extends ServiceImpl<TestPaperMapper, TestPaper> implements TestPaperService {
	@Autowired(required = false)
	private TestPaperMapper testPaperMapper;
	@Override
	public TableVo< TestPaperVo > queryListByPage(TestPaperVo testPaperVo , int page , int pageSize) {
		Page< TestPaper > testPaperPage = new Page<>(page,pageSize);
		TestPaper testPaper  = TestPaperVo.getTestPaper(testPaperVo);
		QueryWrapper< TestPaper > testPaperQueryWrapper = new QueryWrapper<>();
		if (null!= testPaper.getTpName() &&testPaper.getTpName().length()>0){
			StringBuilder stringBuilder = new StringBuilder("%");
			for ( int i=0;i<testPaper.getTpName().length();i++ ){
				stringBuilder.append(testPaper.getTpName().charAt(i)).append("%");
			}
			testPaperQueryWrapper.like("tp_name",stringBuilder);
		}
		if (null!= testPaper.getTpCourseId() &&testPaper.getTpCourseId().length()>0){
			testPaperQueryWrapper.eq("tp_course_id", testPaper.getTpCourseId());
		}
		if ( null!= testPaper.getTpCreateBy() &&testPaper.getTpCreateBy().length()>0 ){
			testPaperQueryWrapper.eq("tp_create_by", testPaper.getTpCreateBy());
		}
		testPaperQueryWrapper.orderByDesc("create_time");
		Page< TestPaper > resultPage = testPaperMapper.selectPage(testPaperPage , testPaperQueryWrapper);
		
		return new TableVo<TestPaperVo>(TestPaperVo.getTestPaperList(resultPage.getRecords()), resultPage.getTotal());
	}
}
