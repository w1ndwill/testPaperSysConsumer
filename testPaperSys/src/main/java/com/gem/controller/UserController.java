package com.gem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gem.entity.User;
import com.gem.service.UserService;
import com.gem.utils.RedisService;
import com.gem.utils.StringUtil;
import com.gem.utils.UUIDUtil;
import com.gem.vo.TableVo;
import com.gem.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@CrossOrigin  //解决跨域问题
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RedisService redisService;
	

	/**
	 * 查询用户 根据用户设置的过滤条件进行分页
	 * @param user
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/queryListByPage")
	public TableVo<UserVo> queryListByPage(User user , int page, int pageSize){
		if (StringUtil.isEmpty(user.getUserId()) ||!user.getUserId().equals(redisService.get(user.getUserId())) ){
			return new TableVo<UserVo>(null,-1L);
		}
		if ( StringUtil.isEmpty(user) ) {
			return null;
		}
		return userService.queryListByPage( user.getIsAdmin(), user.getUserName(), user.getSex(), page, pageSize);
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@PostMapping("/add")
	public Map<String,Object> addUser(@RequestBody User user){
		HashMap< String, Object > map = new HashMap< String, Object >();
		if ( StringUtil.isEmpty(user) ) {
			map.put("success", false);
			map.put("msg", "数据丢失,注册失败!");
			return map;
		}
		QueryWrapper< User > userQueryWrapper = new QueryWrapper<>();
		userQueryWrapper.eq("user_phone", user.getUserPhone());
		
		if ( userService.count(userQueryWrapper)>0 ){
			map.put("success",false);
			map.put("msg", "当前手机号已注册!!");
			return map;
		}
		if ( StringUtil.isEmpty(user.getIsAdmin()) ){
			user.setIsAdmin("N");
		}
		user.setUserId(UUIDUtil.get());
		user.setDeleted(1);
		map.put("success", userService.save(user));
		map.put("userId", user.getUserId());
		return map;
	}
	
	/**
	 * 删除用户 和 批量删除
	 * @param userId
	 * @return
	 */
	@PostMapping("/del")
	public boolean delUserByUserPhone(@RequestParam  String userId){
		System.out.println(userId);
		if ( StringUtil.isEmpty(userId) ) {
			return false;
		}
		QueryWrapper< User > userQueryWrapper = new QueryWrapper<>();
		Object[] ids = userId.split(",");
			userQueryWrapper.in("user_id", ids);
		return userService.remove(userQueryWrapper);
	}

	@PostMapping("/edit")
	public boolean editUser(@RequestBody User user){
		if ( StringUtil.isEmpty(user) ) {
			return false;
		}
		user.setUserPhone(null);
		UpdateWrapper< User > userUpdateWrapper = new UpdateWrapper< User >();
		userUpdateWrapper.eq("user_id", user.getUserId());
		return userService.update(user, userUpdateWrapper);
	}
}

