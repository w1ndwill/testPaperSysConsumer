package com.gem.mapper;

import com.gem.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface UserMapper extends BaseMapper<User> {
    List<User> isAdmin();

    List<User> isAdmin1();

    List<User> isAdmin2();
}
