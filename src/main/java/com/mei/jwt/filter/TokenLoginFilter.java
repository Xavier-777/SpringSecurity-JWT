package com.mei.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mei.jwt.model.LoginData;
import com.mei.jwt.model.SecurityUser;
import com.mei.jwt.security.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 认证过滤器，登录的url为 /test/login，登录成功后响应token
 *
 * @author meizhaowei
 * @since 2021/7/28
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private TokenManager tokenManager;

    private RedisTemplate redisTemplate;

    private AuthenticationManager authenticationManager;

    public TokenLoginFilter(TokenManager tokenManager, RedisTemplate redisTemplate, AuthenticationManager authenticationManager) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
        this.authenticationManager = authenticationManager;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/test/login", "POST"));
        //this.setPostOnly(false);

        //登录地址，登录成功后返回token，这个与setRequiresAuthenticationRequestMatcher 的效果是一样的，
        //但是这个方法是允许所以http方法
        //this.setFilterProcessesUrl("/test/login");
    }

    /**
     * 获取表单的用户名与密码
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //使用 请求体 传递登录参数，更加安全
            LoginData user = new ObjectMapper().readValue(request.getInputStream(), LoginData.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            return authenticationManager.authenticate(token);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 认证成功会调用的方法
     * 返回token
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityUser user = (SecurityUser) authResult.getPrincipal();

        //根据用户名生成token
        String token = tokenManager.createToken(user.getUsername());

        response.setHeader("token", token);

        //将 用户名：角色 放入redis中
        redisTemplate.opsForValue().set(user.getUsername(), user.getRole().getRole_name());
    }

    /**
     * 认证失败会调用的方法
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.getWriter().print("login fail");
    }
}
