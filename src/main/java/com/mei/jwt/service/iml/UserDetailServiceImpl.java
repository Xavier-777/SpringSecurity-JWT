package com.mei.jwt.service.iml;

import com.mei.jwt.entity.Role;
import com.mei.jwt.entity.User;
import com.mei.jwt.model.SecurityUser;
import com.mei.jwt.service.RoleService;
import com.mei.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO.
 *
 * @author meizhaowei
 * @since 2021/7/28
 */
@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.selectByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        //将查出来的密码做 BCrypt 加密，因为我的数据库存的是明文密码；我们正常插入数据库的密码都是加密的
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        //获取权限
        //List<String> list = permissionService.selectPermissionValueByUserId(String.valueOf(user.getId()));

        //获取角色
        Role role = roleService.selectRoleByUsername(username);

        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUserInfo(user);
        //securityUser.setPermissionValueList(list);
        securityUser.setRole(role);

        return securityUser;
    }
}
