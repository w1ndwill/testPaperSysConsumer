package com.gem.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.gem.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;
import java.util.List;

/**
 * package: com.gem.vo
 * ClassName : UserVo
 * 描述: UserVO对象
 *
 * @author QAQ
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserVo {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户id
	 */
	@TableId(value = "user_id" /*, type = IdType.UUID*/)
	private String userId;
	
	/**
	 *  手机号
	 */
	private String userPhone;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 *  密码
	 */
	private String password;
	
	/**
	 *  邮箱
	 */
	private String email;
	
	/**
	 * 是否管理员Y -N
	 */
	private String isAdmin;
	
	/**
	 *  男1--女0
	 */
	private String sex;
	
	/**
	 * 用户头像路径
	 */
	private String photo;

	private Integer job;
	
	/**
	 * 	逻辑删除
	 */
	@TableLogic
	private String deleted;
	
	
	public static List< UserVo > getUserVO(List< User > userList) {
		List< UserVo > userVoList = new LinkedList<>();
		for ( User user : userList ) {
			UserVo userVO = getUserVO(user);
			userVoList.add(userVO);
		}
		return userVoList;
	}
	
	public static UserVo getUserVO(User user) {  // 1
		if ( user == null ) {
			return null;
		}
		UserVo userVO = new UserVo();
		userVO.userId = user.getUserId();
		userVO.userPhone = user.getUserPhone();
		userVO.userName = user.getUserName();
		userVO.password = null;
		userVO.email = user.getEmail();
		userVO.isAdmin = ("Y".equals(user.getIsAdmin())) ? "管理员" : "教师";
		userVO.job = user.getJob();
		if ( null != user.getSex() ) {
			userVO.sex = "1".equals(user.getSex()) ? "男" : "0".equals(user.getSex()) ?"女":"";
		}
		userVO.photo = user.getPhoto();
		userVO.deleted = user.getDeleted() == 1 ? "正常" : "已删除";
		return userVO;
	}
	
}
