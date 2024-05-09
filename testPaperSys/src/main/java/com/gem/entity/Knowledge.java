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
public class Knowledge implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程知识点id
     */
      @TableId(value = "knowledge_id", type = IdType.ASSIGN_UUID)
    private String knowledgeId;

    /**
     * 知识点名字
     */
    private String knowledgeName;

    /**
     * 知识点描述
     */
    private String knowledgeDescribe;

    /**
     * 课程id
     */
    private String courseId;

    /**
     * 创建时间
     */
      @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
