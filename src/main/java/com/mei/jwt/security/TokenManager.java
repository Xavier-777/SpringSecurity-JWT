package com.mei.jwt.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * 生成token与获取token的值
 *
 * @author meizhaowei
 * @since 2021/7/28
 */
@Component
public class TokenManager {

    //过期时间7天
    private long tokenExpiration = 1000 * 60 * 60 * 24 * 7;

    //盐值，由 Keys.secretKeyFor(SignatureAlgorithm.HS256) 提供
    private String salt = "XAfyXDMlDNhV47eVb60biUvVvHrAEt73v9u8nKajQek=";

    //密钥
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * 根据用户名生成token
     *
     * @param username
     * @return
     */
    public String createToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(key)
                .compact();
        return token;
    }


    /**
     * 根据token字符串得到用户信息
     *
     * @param token
     * @return
     */
    public String getUserInfoFromToken(String token) {
        String username = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return username;
    }
}
