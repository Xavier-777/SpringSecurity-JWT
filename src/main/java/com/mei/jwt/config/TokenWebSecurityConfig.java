package com.mei.jwt.config;

import com.mei.jwt.filter.TokenAuthFilter;
import com.mei.jwt.filter.TokenLoginFilter;
import com.mei.jwt.security.TokenLogoutHandler;
import com.mei.jwt.security.TokenManager;
import com.mei.jwt.security.UnauthEnrtyPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * TODO.
 *
 * @author meizhaowei
 * @since 2021/7/28
 */
@Configuration
public class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new UnauthEnrtyPoint())       //无权限访问
                .and().authorizeRequests()
                .antMatchers("/test/guest").permitAll()
                .antMatchers("/test/user").hasRole("user")
                .antMatchers("/test/admin").hasRole("admin")
                .anyRequest().authenticated()
                .and().logout()
                .logoutUrl("/test/logout")     //设置退出路径
                .addLogoutHandler(new TokenLogoutHandler(tokenManager,redisTemplate))   //退出时所做的逻辑
                .and().addFilter(new TokenLoginFilter(tokenManager,redisTemplate,authenticationManager()))
                .addFilter(new TokenAuthFilter(tokenManager,redisTemplate,authenticationManager()))
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 不进行认证的路径，可直接访问
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //这个是swagger的配置类，我没有配swagger，只是跟着敲了一下
        web.ignoring().antMatchers("/api/**");
    }
}
