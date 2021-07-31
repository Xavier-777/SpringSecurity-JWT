package com.mei.jwt.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.jwt.entity.Role;
import com.mei.jwt.mapper.RoleMapper;
import com.mei.jwt.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * TODO.
 *
 * @author meizhaowei
 * @since 2021/7/29
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public Role selectRoleByUsername(String username) {
        return baseMapper.selectRoleByUsername(username);
    }
}

