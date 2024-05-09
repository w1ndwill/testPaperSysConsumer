package com.gem.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaperWdVo{

    @TableId(value = "pw_id", type = IdType.AUTO)
    private Integer pw_id;

    private String pw_name;

    private String pw_create_by;

    private String pw_course;

    private String pw_address;

    private String pw_describe;

    private String pw_key;

    private String pw_auditor1;

    private String pw_auditor2;

    @TableField(fill = FieldFill.INSERT)
    private Date pw_addTime;


}

