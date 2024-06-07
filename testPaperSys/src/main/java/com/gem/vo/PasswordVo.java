package com.gem.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PasswordVo {
	private String userId;
	private String oldPassword;
	private String newPassword;
	private String rePassword;
	private String userName;
	private String phone;
	/**
	 * 验证码
	 */
	private String validate;
}
