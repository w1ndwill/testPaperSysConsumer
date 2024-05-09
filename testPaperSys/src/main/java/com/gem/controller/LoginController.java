package com.gem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gem.entity.User;
import com.gem.service.UserService;
import com.gem.utils.RedisService;
import com.gem.vo.PasswordVo;
import com.gem.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * package: com.gem.controller
 * ClassName : LoginController
 * 描述:
 *
 * @author QAQ
 */
@RestController
@CrossOrigin  //解决跨域问题
@RequestMapping("/user")
public class LoginController {
	@Autowired
	private UserService userService;
	@Autowired
	private RedisService redisService;
	
	/**
	 * 登录
	 * @param user
	 * @return
	 */
	@PostMapping("/login")
	public UserVo loginCheck(@RequestBody User user) {
		QueryWrapper< User > userQueryWrapper = new QueryWrapper<>();
		userQueryWrapper.eq("user_phone" , user.getUserPhone());
		userQueryWrapper.eq("password" , user.getPassword());
		UserVo userVO = UserVo.getUserVO(userService.getOne(userQueryWrapper));
		redisService.setSerializer(new StringRedisSerializer());
		if ( userVO == null ) {
			return null;
		}
		//设计Redis缓存120分钟
		redisService.set(userVO.getUserId() , userVO.getUserId() , 60 * 60 * 2);
		return userVO;
	}
	
	/**
	 * 登出..
	 *
	 * @param userId
	 */
	@PostMapping("/loginOut")
	public void login(@RequestParam String userId) {
		redisService.del(userId);
		System.out.println(userId + ":退出登录...");
	}
	
	/**
	 * 修改密码
	 * @param passwordVo
	 * @return
	 */
	@PostMapping("/changePassword")
	public Map< String, Object > changePassword(@RequestBody PasswordVo passwordVo) {
		HashMap< String, Object > map = new HashMap<>();
		if ( passwordVo.getOldPassword() == null || passwordVo.getNewPassword() == null || null == passwordVo.getUserId() ) {
			map.put("success" , false);
			map.put("msg" , "数据丢失，修改失败！！");
			return map;
		}
		QueryWrapper< User > userQueryWrapper = new QueryWrapper<>();
		userQueryWrapper.eq("user_id" , passwordVo.getUserId());
		userQueryWrapper.eq("password" , passwordVo.getOldPassword());
		if ( userService.count(userQueryWrapper) == 0 ) {
			map.put("success" , false);
			map.put("msg" , "旧密码错误,请重新输入");
			return map;
		}
		UpdateWrapper< User > userUpdateWrapper = new UpdateWrapper<>();
		userUpdateWrapper.eq("user_id" , passwordVo.getUserId());
		userUpdateWrapper.set("password" , passwordVo.getNewPassword());
		map.put("success" , userService.update(userUpdateWrapper));
		return map;
	}
	
	/**
	 * 用户注册
	 *
	 * @return
	 */
	@PostMapping("/register")
	public void getCourse(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException { //注册 重定向到添加用户
		request.getRequestDispatcher("/user/add").forward(request , response);
	}
}
