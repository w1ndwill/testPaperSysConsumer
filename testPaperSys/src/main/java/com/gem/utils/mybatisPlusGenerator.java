package com.gem.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

public class mybatisPlusGenerator {
	public static void main(String[] args) {
		// 需要构建一个代码自动生成器对象
		AutoGenerator mpg = new AutoGenerator();
		// 配置策略// 1、全局配置
		GlobalConfig gc = new GlobalConfig();
		String projectPath = System.getProperty("user.dir");
		gc.setOutputDir(projectPath + "/testPaperSys/src/main/java");
//		gc.setOutputDir("E:/java");
		gc.setAuthor("2536320610@qq.com"); //作者名称
		
		gc.setOpen(false);
		gc.setFileOverride(true); // 是否覆盖
		gc.setServiceName("%sService"); // 去Service的I前缀
		gc.setIdType(IdType.ASSIGN_UUID);//ID自增方式
		
		gc.setDateType(DateType.ONLY_DATE);
		
		mpg.setGlobalConfig(gc);
		
		//2、设置数据源
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl("jdbc:mysql://localhost:3306/testPaperSys?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&autoReconnect=true&rewriteBatchedStatements=true");
		dsc.setDriverName("com.mysql.cj.jdbc.Driver");
		dsc.setUsername("root");
		dsc.setPassword("0");
		dsc.setDbType(DbType.MYSQL);
		mpg.setDataSource(dsc);
		
		//3、包的配置
		PackageConfig pc = new PackageConfig();
		pc.setParent("/");
		//pc.setModuleName("dddd");
		pc.setEntity("com/gem/entity");
//		pc.setMapper("mapper");
//		pc.setService("service");
//		pc.setController("controller");
		mpg.setPackageInfo(pc);
		
		StrategyConfig strategy = new StrategyConfig();
		strategy.setInclude("user" , "question" , "question_bank" ,
				"course" , "knowledge" , "test_paper" , "question_choice");// 设置要映射的表名
//		strategy.setInclude("knowledge");
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		strategy.setEntityLombokModel(true); // 自动lombok；
		strategy.setLogicDeleteFieldName("deleted");// 自动填充配置
		
		//设置自动生成时间
		TableFill gmtCreate = new TableFill("create_time" , FieldFill.INSERT);
		TableFill gmtModified = new TableFill("gmt_modified" , FieldFill.INSERT_UPDATE);
		ArrayList< TableFill > tableFills = new ArrayList<>();
		tableFills.add(gmtCreate);
		tableFills.add(gmtModified);
		strategy.setTableFillList(tableFills);
		
//		strategy.setVersionFieldName("version");// 乐观锁
		
		strategy.setRestControllerStyle(true);
		strategy.setControllerMappingHyphenStyle(true); //localhost:8080/hello_id_2
		mpg.setStrategy(strategy);
		mpg.execute(); //执行
	}
}
