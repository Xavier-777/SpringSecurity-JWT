package com.mei.jwt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.jwt.entity.User;

/**
 * TODO.
 *
 * @author meizhaowei
 * @since 2021/7/28
 */
public interface UserService extends IService<User> {

    User selectByUsername(String username);
}
