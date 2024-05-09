package com.gem.controller;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.gem.vo.KeyVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gem.entity.Key;
import com.gem.entity.TestPaper;
import com.gem.service.KeyService;
import com.gem.utils.RedisService;
import com.gem.utils.StringUtil;
import com.gem.vo.TableVo;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin // 解决跨域问题
@RequestMapping("/api/keys")
public class KeyController {
    @Autowired
    private KeyService keyService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/queryKey")
    public String queryKey(String keyOwner) {
        System.out.println("keyOwner: " + keyOwner);
        System.out.println("publicKey: " + keyService.query(keyOwner));
        return keyService.query(keyOwner);
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateKeyPair(@RequestBody Map<String, String> body) throws NoSuchAlgorithmException {
        String keyOwner = body.get("keyOwner");
        boolean overwrite = Boolean.parseBoolean(body.get("overwrite")); // 是否覆盖
        Key existingKey = keyService.selectKeyByOwner(keyOwner);
        Map<String, String> result = null; // 在这里定义 result 变量
        if (existingKey != null && !overwrite) {
            // 如果存在密钥对且不覆盖，则返回自定义的消息和状态码
            Map<String, String> response = new HashMap<>();
            response.put("status", String.valueOf(409));
            response.put("message", "密钥对已存在，且不允许覆盖");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (existingKey != null) {
            // 如果存在密钥对且允许覆盖，则更新密钥对
            result = keyService.generate(keyOwner);
            keyService.updateKey(result.get("publicKey"), keyOwner);
        } else {
            // 如果不存在密钥对，则生成新的密钥对
            result = keyService.generate(keyOwner);
            keyService.insertKey(result.get("publicKey"), keyOwner);
        }
        Map<String, String> keys = new HashMap<>();
        keys.put("publicKey", result.get("publicKey"));
        keys.put("privateKey", result.get("privateKey"));
        return new ResponseEntity<>(keys, HttpStatus.OK);
    }

    @PostMapping("/getPublicKey")
    public ResponseEntity<Map<String, String>> getPublicKey(@RequestBody Map<String , String> body) {
        String keyowner = body.get("username");
        Key existingKey = keyService.selectKeyByOwner(keyowner);
        if (existingKey == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "没有找到对应的密钥");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, String> keys = new HashMap<>();
        keys.put("publicKey", existingKey.getPublicKey());
        return new ResponseEntity<>(keys, HttpStatus.OK);
    }

}