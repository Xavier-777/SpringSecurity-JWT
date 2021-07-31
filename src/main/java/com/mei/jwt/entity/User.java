package com.mei.jwt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * TODO.
 *
 * @author meizhaowei
 * @since 2021/7/29
 */
@Data
@TableName("users")
public class User {
    private int id;
    private String username;
    private String password;
}
