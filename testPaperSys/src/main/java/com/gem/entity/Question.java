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
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 试题id
     */
      @TableId(value = "que_id", type = IdType.ASSIGN_UUID)
    private String queId;

    /**
     * 所属的课程id
     */
    private String queCourseId;

    /**
     * 所属的题库id
     */
    private String queBankId;

    /**
     * 创建人--跟user_id就名字不一样
     */
    private String queCreateBy;

    /**
     * 知识点列表---
     */
    private String knowledgeIds;

    /**
     * 试题内容
     */
    private String queContent;

    /**
     * 题型,判断题题(bool),简答题(simple)
     */
    private String queType;

    /**
     * 参考答案---
     */
    private String queAnswer;

    /**
     * 答案解析
     */
    private String queAnswerDescribe;

    /**
     * 创建时间
     */
      @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    /**
     * 题目难度(1-10)
     */
    private Integer queDifficulty;

}
