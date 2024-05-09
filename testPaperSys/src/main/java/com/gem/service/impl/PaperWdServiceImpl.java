package com.gem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.entity.PaperWd;
import com.gem.entity.User;
import com.gem.mapper.PaperWdMapper;
import com.gem.mapper.UserMapper;
import com.gem.service.PaperWdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@Service
public class PaperWdServiceImpl extends ServiceImpl<PaperWdMapper, PaperWd> implements PaperWdService {
    @Autowired(required = false)
    private PaperWdMapper paperWdMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAdmin() {
        System.out.println("getAdmin:"+ userMapper.isAdmin());
        return userMapper.isAdmin();
    }

    @Override
    public void insertPaperWd(String papername, String papercreateBy, String course, String paperurl, String paperkey, String iv, String auditor1, String auditor2) {
        paperWdMapper.insertPaperWd1(papername, papercreateBy, course, paperurl, paperkey, iv, auditor1, auditor2);
    }

    @Override
    public List<PaperWd> queryPaperWdByPage(String username, int page, int pageSize) {
        if (page <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page and pageSize must be greater than 0");
        }
        return paperWdMapper.selectPaperWdByPage(username, page, pageSize);
    }

    @Override
    public void checkPaper(String paperId, String approved, String remark, String paperKey) {
        // 从数据库中获取试卷
        paperWdMapper.editStatus(paperId, approved, remark, paperKey);
    }

}