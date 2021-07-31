package com.mei.jwt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.jwt.entity.Role;

/**
 * TODO.
 *
 * @author meizhaowei
 * @since 2021/7/29
 */
public interface RoleService extends IService<Role> {
    Role selectRoleByUsername(String username);
}
