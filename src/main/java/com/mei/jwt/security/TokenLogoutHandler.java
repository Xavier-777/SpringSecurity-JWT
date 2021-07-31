package com.mei.jwt.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出处理器
 *
 * @author meizhaowei
 * @since 2021/7/28
 */
public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;

    private RedisTemplate redisTemplate;

    public TokenLogoutHandler(TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            String username = tokenManager.getUserInfoFromToken(token);

            //从redis中移除token
            redisTemplate.delete(username);
        }
        try {
            response.getWriter().print("logout success");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
