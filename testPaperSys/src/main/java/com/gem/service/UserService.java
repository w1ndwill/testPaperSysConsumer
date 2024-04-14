package com.gem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.entity.User;
import com.gem.vo.TableVo;
import com.gem.vo.UserVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface UserService extends IService<User> {
	
	/**
	 * 查询用户 根据用户设置的过滤条件进行分页
	 * @param isAdmin
	 * @param name
	 * @param sex
	 * @param page
	 * @param pageSize
	 * @return
	 */
	TableVo< UserVo > queryListByPage(String isAdmin , String name , String sex , int page , int pageSize);
	
	/**
	 * 通过创建人(用户id) 获取用户名
	 * @param userId
	 * @return
	 */
	String getUserName(String userId);
}
