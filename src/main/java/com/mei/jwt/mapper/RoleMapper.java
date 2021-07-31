package com.mei.jwt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.jwt.entity.Role;

/**
 * TODO.
 *
 * @author meizhaowei
 * @since 2021/7/29
 */
public interface RoleMapper extends BaseMapper<Role> {

    Role selectRoleByUsername(String username);
}
