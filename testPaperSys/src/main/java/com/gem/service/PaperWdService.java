package com.gem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.entity.PaperWd;
import com.gem.entity.User;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PaperWdService extends IService<PaperWd> {
    List<User> getAdmin();

    void insertPaperWd(String papername, String papercreateBy, String course, String paperurl, String paperkey,String iv, String auditor1, String auditor2);

    List<PaperWd> queryPaperWdByPage(String username, int page, int pageSize);

    void checkPaper(String paperId, String approved, String remark, String paperKey);

}
