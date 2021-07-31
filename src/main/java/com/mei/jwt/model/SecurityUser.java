package com.mei.jwt.model;

import com.mei.jwt.entity.Role;
import com.mei.jwt.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 自定义UserDetails
 *
 * @author meizhaowei
 * @since 2021/7/28
 */
@Data
public class SecurityUser implements UserDetails {

    //transient 表示当前属性不需要序列化
    private transient User currentUserInfo;

    //当前权限集合
    private List<String> permissionValueList;

    //当前角色
    private Role role;

    public SecurityUser() {
    }

    public SecurityUser(User user) {
        if (user != null) {
            this.currentUserInfo = user;
        }
    }

    /**
     * 将 List<String> permissionValueList 封装成 Collection<? extends GrantedAuthority> 返回出去
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        //加入添加权限
//        for (String permissionValue : permissionValueList) {
//            if (permissionValue == null || "".equals(permissionValue)) {
//                continue;
//            }
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissionValue);
//            authorities.add(authority);
//        }
        List<GrantedAuthority> list = AuthorityUtils.commaSeparatedStringToAuthorityList(role.getRole_name());
        authorities.addAll(list);
        return authorities;
    }

    @Override
    public String getPassword() {
        return currentUserInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return currentUserInfo.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
