package com.gem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.entity.PaperWd;
import com.gem.entity.User;
import com.gem.service.PaperWdService;
import com.gem.service.UserService;
import com.gem.utils.RedisService;
import com.gem.utils.StringUtil;
import com.gem.vo.TableVo;
import com.gem.vo.TestPaperVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/paperWd")
public class PaperWdController {

    @Autowired
    private PaperWdService paperWdService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;

    @RequestMapping("/getAdmin1")
    public List<User> getAdmin1() {
        return paperWdService.getAdmin1();
    }

    @RequestMapping("/getAdmin2")
    public List<User> getAdmin2() {
        return paperWdService.getAdmin2();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 验证文件是否为空，以及是否为允许的类型、大小等
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "文件为空"));
            }
            System.out.println("file:"+file);
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            System.out.println("uniqueFileName:"+uniqueFileName);
            // 获取或创建上传目录（假设在项目根目录下的uploads文件夹）
            File uploadDirectory = new File("D:\\JavaEE_project\\graduation-project-master\\testPaperSysConsumer\\testPaperSys\\src\\main\\resources\\static\\uploads");
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }

            // 完整的文件路径
            File destFile = new File(uploadDirectory, uniqueFileName);
            System.out.println("destFile:"+destFile);
            file.transferTo(destFile); // 保存文件
            if(destFile.exists() && destFile.length() > 0) {
                System.out.println("上传文件成功");
            } else {
                System.out.println("上传文件失败");
            }
            return ResponseEntity.ok().body(Map.of("success", true, "message", "上传文件成功", "fileUrl", uploadDirectory + "\\" + uniqueFileName));
        } catch (IOException e) {
            System.out.println("上传文件失败");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "上传文件失败"));
        }
    }

        @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam("papername") String papername,
                     @RequestParam("papercreateBy") String papercreateBy,
                     @RequestParam("paperurl") String paperurl,
                     @RequestParam("paperkey") String paperkey,
                     @RequestParam("papertype") String paperType,
                     @RequestParam("iv") String iv,
                     @RequestParam("auditor1") String auditor1,
                     @RequestParam("auditor2") String auditor2,
                     @RequestParam("course") String course) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            paperWdService.insertPaperWd(papername, papercreateBy, course, paperurl, paperkey, paperType, iv, auditor1, auditor2);
            map.put("success", true);
            map.put("message", "保存成功");
            return ResponseEntity.ok().body(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("message", "保存失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

    @RequestMapping("/queryPaperWdByPage")
    public List<PaperWd> queryPaperWdByPage(@RequestParam("username") String username, int page, int pageSize) {
        if (page <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page and pageSize must be greater than 0");
        }
        System.out.println("queryPaperWdByPage::"+ paperWdService.queryPaperWdByPage(username, page, pageSize));
        return paperWdService.queryPaperWdByPage(username, page, pageSize);
    }

    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody Map<String, Object> params) {
        try {
            // 提取参数
            String paperId = String.valueOf(params.get("paperID"));
            String approved = (String) params.get("approved");
            String remark = (String) params.get("remark");
            String paperKey = (String) params.get("paperKey");
            System.out.println("paperId:"+paperId);
            System.out.println("approved:"+approved);
            System.out.println("remark:"+remark);

            // 调用服务层方法进行处理
            paperWdService.checkPaper(paperId, approved, remark, paperKey);

            // 返回成功响应
            return ResponseEntity.ok().body(Map.of("success", true, "message", "审批成功"));
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();

            // 返回失败响应
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "审批失败"));
        }
    }

    @RequestMapping("/getPapNo")
    public int getQueNo(){
        return paperWdService.count();
    }


}