package com.gem.controller;

import com.gem.utils.RSAKeyUtil;
import org.apache.velocity.shaded.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * package: com.gem.controller
 * ClassName : UpLoadFileController
 * 描述:
 *
 * @author QAQ*/
@RestController
@CrossOrigin  //解决跨域问题
@RequestMapping("/upload")
public class UpLoadFileController {
	
	@Value("${server.port}")
	private String port;


	//上传文件
	public byte[] uploadFile(InputStream file){
		//调用RSAKeyUtil对文件进行加密
		try {
			byte[] bytes = file.readAllBytes();
			byte[] encrypt = RSAKeyUtil.encrypt(bytes);


			return encrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@RequestMapping("/photo")
	public Map<String,Object> uploadPhoto(MultipartFile file){
		Map<String, Object> map = new HashMap<>();
		String projectPath = System.getProperty("user.dir");
		String serverPath = "/testPaperSys/src/main/resources/static/image/";
		String targetPath=projectPath+serverPath;
		try {
			String oldFileName = file.getOriginalFilename();
			String lastName = FilenameUtils.getExtension(oldFileName);
			String newFileName= UUID.randomUUID().toString().substring(0,15)+"."+lastName;
			File file2 = new File(targetPath,newFileName);
			if (!file2.exists()) {
				file2.mkdirs();
			}
			file.transferTo(file2);
			String ipAddress = InetAddress.getLocalHost().getHostAddress();
			map.put("success", true);
			map.put("imageUrl", /*"http://localhost:"+port+*/"/image/"+newFileName);
//			map.put("photoUrl", "/image/"+newFileName);
			return map;
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", e.getMessage());
			return map;
		}
	}
}
