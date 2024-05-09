package com.gem.service;

import com.gem.entity.Key;
import com.gem.vo.KeyVo;
import com.gem.vo.TableVo;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface KeyService {
    Map<String, String> generate(String keyOwner) throws NoSuchAlgorithmException;

    List<Key> selectAllKeys( int page, int limit);

    String query(String keyOwner);

    Key selectKeyByOwner(String keyOwner);

    void insertKey(String publicKey, String keyOwner);

    void updateKey(String publicKey, String keyOwner);

}