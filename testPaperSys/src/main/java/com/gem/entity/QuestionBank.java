package com.gem.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class QuestionBank implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 题库id
     */
      @TableId(value = "que_bank_id", type = IdType.ASSIGN_UUID)
    private String queBankId;

    /**
     * 题库名字
     */
    private String queBankName;

    /**
     * 题库描述
     */
    private String queBankDescribe;

    /**
     * 课程id,--外键
     */
    private String queBankCourseId;

    /**
     * 创建时间
     */
      @TableField(fill = FieldFill.INSERT)
    private Date createTime;
	
	
	private String createBy;
}
