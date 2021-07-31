package com.mei.jwt.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO.
 *
 * @author meizhaowei
 * @since 2021/7/28
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 无认证
     *
     * @return
     */
    @RequestMapping("/guest")
    public String guest() {
        return "guest";
    }

    /**
     * 需要认证
     *
     * @return
     */
    @RequestMapping("/hello")
    public String hello() {
        return "hello somebody!  welcome you";
    }

    /**
     * admin 角色才能使用
     *
     * @return
     */
    @RequestMapping("/admin")
    public String admin() {
        return "welcome you Mr.admin!";
    }

    /**
     * role 角色才能访问
     *
     * @return
     */
    @RequestMapping("/user")
    public String user() {
        return "hello user";
    }
}
