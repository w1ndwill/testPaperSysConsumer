package com.gem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.entity.User;
import com.gem.mapper.UserMapper;
import com.gem.service.UserService;
import com.gem.vo.TableVo;
import com.gem.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 2536320610@qq.com
 * @since 2021-01-21
 */
@Service
public class UserServiceImpl extends ServiceImpl< UserMapper, User > implements UserService {
	@Autowired(required = false)
	private UserMapper userMapper;
	
	@Override
	public TableVo< UserVo > queryListByPage(String isAdmin , String userName , String sex , int page , int pageSize) {
		Page< User > userPage = new Page<>(page , pageSize);
		QueryWrapper< User > userWrapper = new QueryWrapper<>();
		// studentWrapper.orderByDesc(constantUtil.EMAIL);
		if ( !"".equals(userName) ) {
			StringBuffer stringBuffer = new StringBuffer("%");
			for ( int i = 0; i < userName.length(); i++ ) {
				stringBuffer.append(userName.charAt(i)).append("%");
			}
			userWrapper.like("user_name" , stringBuffer);
		}
		if ( !"".equals(sex) ) {
			userWrapper.eq("sex" , sex);
		}
		if ( !"".equals(isAdmin) ) {
			userWrapper.eq("is_admin" , isAdmin);
		}
		userWrapper.orderByDesc("create_time");
		Page< User > resultPage = userMapper.selectPage(userPage , userWrapper);
		return new TableVo< UserVo >(UserVo.getUserVO(resultPage.getRecords()) , resultPage.getTotal());
	}
	
	@Override
	public String getUserName(String userId) {
		QueryWrapper< User > userQueryWrapper = new QueryWrapper<>();
		userQueryWrapper.eq("user_id" , userId);
		userQueryWrapper.select("user_name");
		User user = userMapper.selectOne(userQueryWrapper);
		if ( null == user ) {
			return "暂无";
		}
		return user.getUserName();
	}
}
