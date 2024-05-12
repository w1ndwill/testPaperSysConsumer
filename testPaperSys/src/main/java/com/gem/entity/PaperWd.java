package com.gem.entity;

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
public class PaperWd implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "pw_id", type = IdType.AUTO)
    private Integer pw_id;

    private String pw_name;

    private String pw_create_by;

    private String pw_course;

    private String pw_address;

    private String pw_describe;

    private String pw_key;

    private String pw_type;

    private String iv;

    private String pw_status;

    private String pw_auditor1;

    private String pw_auditor2;

      @TableField(fill = FieldFill.INSERT)
    private Date pw_addTime;

    public PaperWd(Integer pw_id, String pw_name, String pw_create_by, String pw_course, String pw_address, String pw_describe, String pw_key, String pw_type,String iv, String pw_status,String pw_auditor1, String pw_auditor2, Date pw_addTime) {
        this.pw_id = pw_id;
        this.pw_name = pw_name;
        this.pw_create_by = pw_create_by;
        this.pw_course = pw_course;
        this.pw_address = pw_address;
        this.pw_describe = pw_describe;
        this.pw_key = pw_key;
        this.pw_type = pw_type;
        this.iv = iv;
        this.pw_status = pw_status;
        this.pw_auditor1 = pw_auditor1;
        this.pw_auditor2 = pw_auditor2;
        this.pw_addTime = pw_addTime;
    }

}
