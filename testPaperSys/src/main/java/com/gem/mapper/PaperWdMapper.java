package com.gem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.entity.PaperWd;

import java.util.List;

public interface PaperWdMapper extends BaseMapper<PaperWd> {
    void insertPaperWd1(String papername, String papercreateBy, String course, String paperurl, String paperkey, String paperType, String iv, String auditor1, String auditor2);

    List<PaperWd> selectPaperWdByPage(String username, int page, int pageSize);

    PaperWd selectById(String paperId);

    void editStatus(String paperId, String status, String remark, String paperKey);
}