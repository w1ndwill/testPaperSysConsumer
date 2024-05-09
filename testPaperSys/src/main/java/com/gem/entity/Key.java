package com.gem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
public class Key {
    private static final long serialVersionUID = 1L; //序列化 这段需要添加吗
      @TableId(value = "key_id", type = IdType.ASSIGN_UUID)
    private Integer keyId;

    private String publicKey;

    private String privateKey;

    private String key_owner;
}
