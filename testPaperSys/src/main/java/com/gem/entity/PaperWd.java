package com.gem.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
public class PaperWd implements Serializable {
    private static final long serialVersionUID = 1L;

      @TableId(value = "pw_id", type = IdType.ASSIGN_UUID)

    private int pw_id;

    private String pw_name;

    private String pw_create_by;

    private String pw_course_id;

    private String pw_address;

    private String pw_describe;

      @TableField(fill = FieldFill.INSERT)
    private Date pw_addTime;
}
