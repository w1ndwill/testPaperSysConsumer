package com.gem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
      @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    private String userId;

    /**
     * 手机号
     */
    private String userPhone;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否管理员Y -N
     */
    private String isAdmin;

    /**
     * 男1--女0
     */
    private String sex;

    /**
     * 用户头像路径
     */
    private String photo;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
      @TableField(fill = FieldFill.INSERT) // 自动填充
    private Date createTime;


}
