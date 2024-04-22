package com.gem.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class PaperWdVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "pw_id", type = IdType.ASSIGN_UUID)
    private int pw_id;

    private String pw_name;

    private String pw_create_by;

    private String pw_course_id;

    private String pw_address;

    private String pw_describe;


}
