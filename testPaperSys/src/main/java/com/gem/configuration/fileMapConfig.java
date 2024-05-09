package com.gem.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态文件映射配置类
 */
@Configuration
public class fileMapConfig implements WebMvcConfigurer {
	
	String projectPath = System.getProperty("user.dir");
	String serverPath = "/testPaperSys/src/main/resources/static/image/";
	String filePath = projectPath + serverPath;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/** 路径映射 */
		registry.addResourceHandler("/image/**").addResourceLocations("file:" + filePath);
	}
}
/*  直接将整个resources映射出去
spring
*   resources:
    static-locations: file:E:\SMALLKY_dev\BS_dev\static
* */
