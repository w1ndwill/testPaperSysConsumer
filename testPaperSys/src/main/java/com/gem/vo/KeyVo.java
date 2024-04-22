package com.gem.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gem.entity.Key;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data

public class KeyVo {
    private static final long serialVersionUID = 1L;

    @TableId(value = "key_id", type = IdType.ASSIGN_UUID)
    private Integer keyId;

    private String publicKey;

    private String privateKey;



    public static List< KeyVo > getKeyVo(List< Key > keyList) {
        List< KeyVo > keyVoList = new LinkedList<>();
        for (Key key : keyList) {
            KeyVo keyVo = new KeyVo();
            keyVoList.add(keyVo);
        }
        return keyVoList;
    }

    public static KeyVo getKeyVo(Key key) {
        if (key == null) {
            return null;
        }
        KeyVo keyVo = new KeyVo();
        keyVo.keyId = key.getKeyId(); // 修改的地方
        keyVo.publicKey = key.getPublicKey();
        keyVo.privateKey = key.getPrivateKey();
        return keyVo;
    }

}
