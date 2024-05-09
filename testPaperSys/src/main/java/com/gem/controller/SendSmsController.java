package com.gem.controller;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gem.entity.User;
import com.gem.service.UserService;
import com.gem.utils.RedisService;
import com.gem.utils.StringUtil;
import com.gem.vo.PasswordVo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * package: com.gem.controller
 * ClassName : sendSmsController
 * 描述:
 *
 * @author QAQ
 */
@RequestMapping("/user")
@CrossOrigin  //解决跨域问题
@RestController
@PropertySource(value = "classpath:application.properties",encoding = "UTF-8")
public class SendSmsController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RedisService redisService;
	
	@Value("${alisms.accessKeyId}")
	private String accessKeyId; 
	@Value("${alisms.accessSecret}")
	private String accessSecret; 
//	@Value("${alisms.signName}")
	private String signName="ABC商城";
	@Value("${alisms.templateCode}")
	private String templateCode;
	/**
	 * 验证码来源
	 */
	public static final String VERIFY_CODES = "1234567890";
	
	@PostMapping("/reSetPassword")
	public Map< String, Object > reSetPassword(@RequestBody PasswordVo passwordVo) {
		HashMap< String, Object > map = new HashMap<>();
		if ( StringUtil.isEmpty(passwordVo) || StringUtil.isEmpty(passwordVo.getUserId()) || StringUtil.isEmpty(passwordVo.getNewPassword()) ) {
			map.put("msg" , "数据丢失,请重试!!");
			map.put("success" , false);
			return map;
		}
		if ( !passwordVo.getNewPassword().equals(passwordVo.getRePassword()) ) {
			map.put("msg" , " 两次密码不一致,请重新输入!!");
			map.put("success" , false);
			return map;
		}
		UpdateWrapper< User > userUpdateWrapper = new UpdateWrapper<>();
		userUpdateWrapper.eq("user_id" , passwordVo.getUserId());
		userUpdateWrapper.set("password" , passwordVo.getNewPassword());
		boolean update = userService.update(userUpdateWrapper);
		if ( !update ) {
			map.put("msg" , "未知错误原因!!");
		}
		map.put("success" , update);
		return map;
	}
	
	/**
	 * 检查验证码是否有效
	 *
	 * @param passwordVo
	 * @return
	 */
	@PostMapping("/checkValidate")
	public boolean checkSms(@RequestBody PasswordVo passwordVo) {
		if ( StringUtil.isEmpty(passwordVo) || StringUtil.isEmpty(passwordVo.getValidate())  || StringUtil.isEmpty(passwordVo.getPhone()) ) {
			return false;
		}
		// 不区分大小写
		String redisValidate = (String) redisService.get(passwordVo.getPhone().toUpperCase());
		String validate = passwordVo.getValidate().toUpperCase();
		return validate.equals(redisValidate);
	}
	
	/**
	 * 发生验证码到手机
	 *
	 * @param passwordVo
	 * @return
	 */
	@PostMapping("/sendSms")
	public Map< String, Object > sendSms(@RequestBody PasswordVo passwordVo) {
		HashMap< String, Object > resultMap = new HashMap<>();
		if ( StringUtil.isEmpty(passwordVo.getPhone())) {
			resultMap.put("msg" , "数据丢失请重试!!");
			resultMap.put("success" , false);
			return resultMap;
		}
		QueryWrapper< User > userQueryWrapper = new QueryWrapper<>();
		userQueryWrapper.eq("user_name" , passwordVo.getUserName());
		userQueryWrapper.eq("user_phone" , passwordVo.getPhone());
		if ( userService.count(userQueryWrapper) == 0 ) {
			resultMap.put("msg" , "用户名和手机号对不上");
			resultMap.put("success" , false);
			return resultMap;
		}
		String randomCode = getVerifyCode(6 , VERIFY_CODES);
		Map< String, String > map = new HashMap< String, String >();
		if("xxx".equals(accessSecret)|| "xxx".equals(accessKeyId)|| "xxx".equals(signName)|| "xxx".equals(templateCode)){
			resultMap.put("msg" , "很抱歉,你的阿里云短信账户已过期!!");
			resultMap.put("success" , false);
			return resultMap;
		}
		map.put("accessKeyId" , accessKeyId);
		map.put("accessSecret" , accessSecret);
		System.out.println("signName=>"+signName);
		map.put("SignName" , signName);
		map.put("TemplateCode" ,templateCode );
		boolean result = aLiYunSendSms(passwordVo.getPhone() , randomCode , map);
		if ( !result ) {
			resultMap.put("msg" , "未知原因");
		}
		redisService.set(passwordVo.getPhone() , randomCode , 60 * 60);
		resultMap.put("success" , result);
		return resultMap;
	}
	
	/**
	 * 发送短信 方法
	 *
	 * @param phoneNumbers 手机号码
	 * @param randomCode   随机验证码,[a-zA-Z0-9],不能是文字
	 * @param paramMap     accessKeyId: AccessKey管理里的accessKeyId
	 *                     accessSecret: AccessKey管理里的Secret
	 *                     signName: 签名名称
	 *                     templateCode: 模版code
	 * @return ok 或者 not_ok
	 */
	private static boolean aLiYunSendSms(String phoneNumbers, String randomCode , Map< String, String > paramMap) {
		//连接阿里云
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou" , paramMap.get("accessKeyId") , paramMap.get("accessSecret"));
		IAcsClient client = new DefaultAcsClient(profile);
		//下面的不要乱动
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain("dysmsapi.aliyuncs.com");
		request.setSysVersion("2017-05-25");
		request.setSysAction("SendSms");
//		request.putQueryParameter("RegionId", "cn-hangzhou");
		//自定义参数
		//手机号码
		request.putQueryParameter("PhoneNumbers" , phoneNumbers);
		//签名名称
		request.putQueryParameter("SignName" , paramMap.get("SignName"));
		//信息模板代码(模版CODE)
		request.putQueryParameter("TemplateCode" , paramMap.get("TemplateCode"));
		//构建验证码//必须是Json格式,不然会报错
		HashMap< String, Object > codeMap = new HashMap< String, Object >();
		codeMap.put("code" , randomCode);
		request.putQueryParameter("TemplateParam" , new Gson().toJson(codeMap));
		
		try {
			CommonResponse response = client.getCommonResponse(request);
			System.out.println(response.getData());
			String infojson = response.getData();
			Gson gson2 = new Gson();
			Map map = gson2.fromJson(infojson , Map.class);
			String codes = map.get("Message").toString();
			return codes.equals("OK");
		} catch ( ClientException e ) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 使用指定源生成验证码
	 *
	 * @param codeSize 验证码长度
	 * @param sources  验证码字符源
	 * @return
	 */
	private String getVerifyCode(int codeSize , String sources) {
		int codesLen = sources.length();
		Random rand = new Random(System.currentTimeMillis());
		StringBuilder verifyCode = new StringBuilder(codeSize);
		for ( int i = 0; i < codeSize; i++ ) {
			verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
		}
		return verifyCode.toString();
	}
}
	
	
	/*
	 * alisms:  yml参数
		  accessKeyId: AccessKey管理里的accessKeyId
		  accessSecret: AccessKey管理里的Secret
		  signName: 短信签名名称
		  templateCode: 模版code
       	@Value("${alisms.accessKeyId}")
		private String accessKeyId;
		@Value("${alisms.accessSecret}")
		private String accessSecret;
		@Value("${alisms.signName}")
		private String signName;
		@Value("${alisms.templateCode}")
		private String templateCode;
		*/
