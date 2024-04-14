package com.gem.utils;

/**
 * package: com.gem.utils
 * ClassName : addQueUtil
 * 描述:
 **/
public class addQueUtil {

	public static void main(String[] args) {

		for(int i=0;i<60;i++){
			//知识点
			// 软件工程
//			String sql ="INSERT INTO `knowledge` VALUES ('ef4eef5257ac4ea79f"+i+"', '软件工程知识点-"+i+"', '软件工程知识点-解释"+i+"', '30c2a810-2b9a-4', '2021-02-13 12:25:0"+i+"');";
			//javawweb
//     		String sql ="INSERT INTO `knowledge` VALUES ('96194391ce8f410bba"+i+"', 'JavaWeb-"+i+"', 'JavaWeb知识点-解释"+i+"', 'e74e7450-c280-4', '2021-03-10 10:17:0"+i+"');";
			//SpringBoot
//     		String sql ="INSERT INTO `knowledge` VALUES ('96194391ce8f410bbc"+i+"', 'SpringBoot知识点-"+i+"', 'SpringBoot知识点-解释"+i+"', 'b275ad41-274c-4', '2021-03-12 12:25:0"+i+"');";
			
			//选择题
			//软件工程
//			String baseKnowledgeId = "ef4eef5257ac4ea79f";
			//Javaweb
//			String baseKnowledgeId = "96194391ce8f410bba";
			//Javaweb
			String baseKnowledgeId = "96194391ce8f410bbc";
			
			int diff = 0;
			
			//随机知识点
			String knowledgeIds = "";
			for(int j=0;j<3;j++){
				int random = (int)(Math.random()*100);
				diff = random %10;
				if(random>=60){
					random-=40;
				}
//				if(random<0){
//					random+=10;
//				}
				knowledgeIds += baseKnowledgeId+random+",";
			}
			//随机选择题答案
//			String answerArr[]={"A","B","C","D"};
//			String answer =answerArr[(int)(Math.random()*100)%4];
			//添加选择题
			//软件工程
//			String sql = "INSERT INTO `testpapersys`.`question_choice` " +
//					"VALUES ('15632d144a3e4a0fa"+i+"', '30c2a810-2b9a-4', '5bee7847-5c87-4', '05ad6e1c-b8de-4', '"+knowledgeIds.substring(0, knowledgeIds.length()-1)+"', '软件工程题目自动添加-内容-"+i+"', '自动添加选项A的内容-"+i+"', '自动添加选项B的内容-"+i+"', '自动添加选项C的内容-"+i+"', '自动添加选项D的内容-"+i+"', '"+answer+"', '软件工程题目自动添加-的解析-"+i+"', '2021-02-22 08:47:0"+i+"',"+diff+");";
			//JavaWeb
//			String sql = "INSERT INTO `testpapersys`.`question_choice` " +
//					"VALUES ('15632d144a3e4a0fb"+i+"', 'e74e7450-c280-4', '0b7ac75bc6844b1f84', '87e7a849-3085-4', '"+knowledgeIds.substring(0, knowledgeIds.length()-1)+"', 'javaWeb题目自动添加-内容-"+i+"', '自动添加选项A的内容-"+i+"', '自动添加选项B的内容-"+i+"', '自动添加选项C的内容-"+i+"', '自动添加选项D的内容-"+i+"', '"+answer+"', 'javaweb题目自动添加-的解析-"+i+"', '2021-03-10 10:22:"+i+"',"+diff+");";
			//springboot
//			String sql = "INSERT INTO `testpapersys`.`question_choice` " +
//					"VALUES ('15632d144a3e4a0fb"+i+"', 'b275ad41-274c-4', 'f6f17ca7-cb57-4', '87e7a849-3085-4', '"+knowledgeIds.substring(0, knowledgeIds.length()-1)+"', 'springboot题目自动添加-内容-"+i+"', '自动添加选项A的内容-"+i+"', '自动添加选项B的内容-"+i+"', '自动添加选项C的内容-"+i+"', '自动添加选项D的内容-"+i+"', '"+answer+"', 'springboot题目自动添加-的解析-"+i+"', '2021-03-10 11:22:"+i+"',"+diff+");";


			String[] answerArr ={"错误【×】","正确【√】"};
			String answer =answerArr[(int)(Math.random()*100)%2];
//			
			// 软件工程
//			String sql = "INSERT INTO `testpapersys`.`question`(`que_id`, `que_course_id`, `que_bank_id`, `que_create_by`, `knowledge_ids`, `que_content`, `que_type`, `que_answer`, `que_answer_describe`, `create_time`) " +
//					"VALUES ('11e9bb6441d541198a"+i+"', '30c2a810-2b9a-4', '5bee7847-5c87-4', '05ad6e1c-b8de-4', '"+knowledgeIds.substring(0, knowledgeIds.length()-1)+"', '软件工程-判断题-"+i+"', 'bool', '"+answer+"', '软件工程-判断题-解析-"+i+"', '2021-02-22 09:36:0"+i+"',"+diff+");";
			//Javaweb
//			String sql = "INSERT INTO `testpapersys`.`question`" +
//					"VALUES ('11e9bb6441d541198b"+i+"', 'e74e7450-c280-4', '0b7ac75bc6844b1f84', '7581b3cd048b4c6193', '"+knowledgeIds.substring(0, knowledgeIds.length()-1)+"', 'Javaweb-判断题-"+i+"', 'bool', '"+answer+"', 'javaweb-判断题-解析-"+i+"', '2021-03-10 10:55:"+i+"',"+diff+");";
			//springboot
//			String sql = "INSERT INTO `testpapersys`.`question`" +
//					"VALUES ('11e9bb6441d541198c"+i+"', 'b275ad41-274c-4', 'f6f17ca7-cb57-4', '87e7a849-3085-4', '"+knowledgeIds.substring(0, knowledgeIds.length()-1)+"', 'springboot-判断题-"+i+"', 'bool', '"+answer+"', 'springboot-判断题-解析-"+i+"', '2021-04-10 11:33:"+i+"',"+diff+");";

			// 简单题
			//软件工程
//			String sql = "INSERT INTO `testpapersys`.`question`(`que_id`, `que_course_id`, `que_bank_id`, `que_create_by`, `knowledge_ids`, `que_content`, `que_type`, `que_answer`, `que_answer_describe`, `create_time`) " +
//					"VALUES ('11e9bb6441d541198b"+i+"', '30c2a810-2b9a-4', '5bee7847-5c87-4', '05ad6e1c-b8de-4', '"+knowledgeIds.substring(0, knowledgeIds.length()-1)+"', '软件工程-简答题-"+i+"', 'simple', '软件工程-简答题-答案-"+i+"', '软件工程-简答题-解析-"+i+"', '2021-02-22 09:37:0"+i+"');";
//			Javaweb
//			String sql = "INSERT INTO `testpapersys`.`question`" +
//					"VALUES ('11e9bb6441d541198c"+i+"', 'e74e7450-c280-4', '0b7ac75bc6844b1f84', '7581b3cd048b4c6193', '"+knowledgeIds.substring(0, knowledgeIds.length()-1)+"', 'Javaweb-简答题-"+i+"', 'simple', 'Javaweb-简答题-答案-"+i+"', 'Javaweb-简答题-解析-"+i+"', '2021-03-10 10:58:"+i+"',"+diff+");";
//			
//			springboot
			String sql = "INSERT INTO `testpapersys`.`question`" +
					"VALUES ('11e9bb6441d541198c"+i+"', 'b275ad41-274c-4', 'f6f17ca7-cb57-4', '7581b3cd048b4c6193', '"+knowledgeIds.substring(0, knowledgeIds.length()-1)+"', 'springboot-简答题-"+i+"', 'simple', 'springboot-简答题-答案-"+i+"', 'springboot-简答题-解析-"+i+"', '2021-02-22 11:38:"+i+"',"+diff+");";
//			
			System.out.println(sql);
		}
	}
}
