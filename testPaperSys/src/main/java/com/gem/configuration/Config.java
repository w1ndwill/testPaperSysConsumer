package com.gem.configuration;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.gem.utils.MetaHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config  {
	/**
	 * mybatis-plus分页插件
	 * @return
	 */
   @Bean
   public PaginationInterceptor paginationInterceptor() {
	   return new PaginationInterceptor();
   }
	
	/**
	 * 自动填充功能
	 * @return
	 */
	@Bean
	public GlobalConfig globalConfig() {
		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setMetaObjectHandler(new MetaHandler());
		return globalConfig;
	}
}
