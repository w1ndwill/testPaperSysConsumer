package com.gem.vo;

import lombok.Data;
import lombok.ToString;

/**
 * Date : 2021/1/27 8:28
 * package: com.gem.vo
 * ClassName : PasswordVo
 * 描述:
 *
 * @author QAQ
 */
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
