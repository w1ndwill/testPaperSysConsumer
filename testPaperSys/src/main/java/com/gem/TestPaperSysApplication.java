package com.gem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gaox
 */
@MapperScan("com.gem.mapper")
@SpringBootApplication
public class TestPaperSysApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestPaperSysApplication.class , args);
	}

}
