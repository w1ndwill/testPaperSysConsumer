package com.gem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.entity.Key;
import com.gem.mapper.KeyMapper;
import com.gem.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Service
public class KeyServiceImpl extends ServiceImpl<KeyMapper, Key> implements KeyService {
    @Autowired(required = false)
    private KeyService keyService;

    @Autowired
    private KeyMapper keyMapper;


    @Override
    public Map<String, String> generate(String keyOwner) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); // 生成密钥对
        keyGen.initialize(512); // 设置密钥长度
        KeyPair pair = keyGen.generateKeyPair(); // 生成密钥对
        PrivateKey privateKey = pair.getPrivate(); // 获取私钥
        PublicKey publicKey = pair.getPublic(); // 获取公钥

        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded()); // 将公钥转换为 Base64 编码
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded()); // 将私钥转换为 Base64 编码

        // 创建一个新的 Key 对象
        Key key = new Key();
        key.setPublicKey(publicKeyString); // 设置公钥
        key.setKey_owner(keyOwner); // 设置生成者

        Map<String, String> keys = new HashMap<>(); // 创建一个 Map 对象
        keys.put("publicKey", publicKeyString);  // 将公钥放入 Map 对象
        keys.put("privateKey", privateKeyString); // 将私钥放入 Map 对象
        return keys; // 返回 Map 对象
    }

    public void insertKey(String publicKey, String keyOwner) {
        keyMapper.insertKey(publicKey, keyOwner);
    }

    public void updateKey(String publicKey, String keyOwner) {
        keyMapper.updateKey(publicKey, keyOwner);
    }

    @Override
    public List<Key> selectAllKeys(int page, int limit) {
        return keyMapper.selectAllKeys(page, limit);
    }

    @Override
    public String query(String keyOwner) {
        QueryWrapper<Key> keyWrapper = new QueryWrapper<>();
        if (keyOwner != null && keyOwner.length() > 0) {
            keyWrapper.eq("key_owner", keyOwner);
        }
        Key result = keyMapper.selectKeyByOwner(keyOwner); // 查询 Key 对象
        System.out.println("keyOwner: " + keyOwner);
        System.out.println("result: " + result);
        if (result != null) {
            return result.getPublicKey(); // 返回公钥
        }
        return null;
    }


    @Override
    public Key selectKeyByOwner(String keyOwner) {
        return keyMapper.selectKeyByOwner(keyOwner);
    }
}