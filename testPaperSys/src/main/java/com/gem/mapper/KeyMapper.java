package com.gem.mapper;

import com.gem.entity.Key;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface KeyMapper extends BaseMapper<Key> {
    void insertKey(@Param("publicKey") String publicKey, @Param("keyOwner") String keyOwner);    /**
     * 根据试卷id获取密钥
     *
     * @param keyOwner
     * @return
     */

    Key selectKeyByOwner(@Param("testPaperId") String keyOwner);

    void updateKey(@Param("publicKey") String publicKey, @Param("keyOwner") String keyOwner);

    List<Key> selectAllKeys( @Param("page") int page, @Param("limit") int limit);
}
