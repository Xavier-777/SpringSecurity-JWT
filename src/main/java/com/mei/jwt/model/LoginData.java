package com.mei.jwt.model;

import lombok.Data;

/**
 * 接收登录的 username & password
 *
 * @author meizhaowei
 * @since 2021/7/28
 */
@Data
public class LoginData {
    private String username;

    private String password;
}
