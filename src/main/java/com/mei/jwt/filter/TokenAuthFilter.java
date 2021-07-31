package com.mei.jwt.filter;

import com.mei.jwt.entity.Role;
import com.mei.jwt.security.TokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 授权过滤器，校验token的有效性
 *
 * @author meizhaowei
 * @since 2021/7/28
 */
@Log4j2
public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;

    private RedisTemplate redisTemplate;

    public TokenAuthFilter(TokenManager tokenManager, RedisTemplate redisTemplate, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 使用token做放行校验
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //授权
        UsernamePasswordAuthenticationToken authRequest = getAuthentication(request);

        //授权失败
        if (authRequest == null) {
            chain.doFilter(request, response);
            return;
        }

        //如果有授权，放到权限上下文（容器）中
        SecurityContextHolder.getContext().setAuthentication(authRequest);
        chain.doFilter(request, response);
    }

    /**
     * 认证token是否合法，若合法，授予响应角色并返回用户权限信息，否则返回null
     *
     * @param request
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {

            //从token中获取username
            String username = tokenManager.getUserInfoFromToken(token);

            //redis中，根据username获取角色
            String role_name = (String) redisTemplate.opsForValue().get(username);


            Collection<GrantedAuthority> authorities = new ArrayList<>();
            List<GrantedAuthority> list = AuthorityUtils.commaSeparatedStringToAuthorityList(role_name);
            authorities.addAll(list);

            // List<String> 转化为 Collection<GrantedAuthority> 类型
//                for (String permissionValue : permissionValuesList) {
//                    SimpleGrantedAuthority auth = new SimpleGrantedAuthority(permissionValue);
//                    authorities.add(auth);
//                }

            if (!StringUtils.isEmpty(username)) {
                return new UsernamePasswordAuthenticationToken(username, token, authorities);
            }
        }
        return null;
    }
}
