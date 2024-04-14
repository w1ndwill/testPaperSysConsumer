package com.gem.service;

import com.gem.entity.TestPaper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.vo.TableVo;
import com.gem.vo.TestPaperVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface TestPaperService extends IService<TestPaper> {
	
	TableVo< TestPaperVo> queryListByPage(TestPaperVo testPaperVo , int page , int pageSize);
}
