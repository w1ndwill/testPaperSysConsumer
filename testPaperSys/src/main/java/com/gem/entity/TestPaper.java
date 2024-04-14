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
public class TestPaper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 试卷id
     */
      @TableId(value = "tp_id", type = IdType.ASSIGN_UUID)
    private String tpId;

    /**
     * 试卷名字
     */
    private String tpName;

    /**
     * 创建人
     */
    private String tpCreateBy;

    /**
     * 所属的课程id
     */
    private String tpCourseId;

    /**
     * 试题的id列表
     */
    private String tpQueIds;

    /**
     * 知识点列表
     */
    private String tpKnowledgeIds;

    /**
     * 试卷描述
     */
    private String tpDescribe;

    /**
     * 创建时间
     */
      @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
