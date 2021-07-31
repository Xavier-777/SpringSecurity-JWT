package com.mei.jwt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * TODO.
 *
 * @author meizhaowei
 * @since 2021/7/29
 */
@Data
@TableName("role")
public class Role {
    private int id;

    @TableField(value = "name")
    private String role_name;

    public void setRole_name(String role_name) {
        this.role_name = "ROLE_" + role_name;
    }
}
