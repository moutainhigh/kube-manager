package com.cgm.kube.config;

import com.cgm.kube.account.service.impl.SysUserServiceImpl;
import com.cgm.kube.config.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * 程序大致流程：
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * 1.Spring Security在用户登录时会加载一次用户信息，放到内存里，需要自己实现loadUserByUsername方法
 * @see org.springframework.security.core.userdetails.UserDetails
 * 2.Spring Security会将用户权限加载到内存，需要实现getAuthorities方法，一般存角色，也可以存接口
 * @see CustomizeFilterInvocationSecurityMetadataSource
 * 3.用户每次请求一个接口的时候，都会经过getAttributes方法，这个方法用来判断这个接口需要什么权限才能访问，和前面的加载权限对应
 * @see CustomizeAccessDecisionManager
 * 4.通过实现decide方法，将用户拥有的权限与当前需要的权限进行匹配，决定是否通过
 *
 * @author cgm
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomizeAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private CustomizeAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private CustomizeAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomizeAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private CustomizeSessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private CustomizeLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private CustomizeAccessDecisionManager accessDecisionManager;

    @Autowired
    private CustomizeFilterInvocationSecurityMetadataSource securityMetadataSource;

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        //获取用户账号密码及权限信息
        return new SysUserServiceImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests().
                withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        // 决策管理器
                        o.setAccessDecisionManager(accessDecisionManager);
                        // 安全元数据源
                        o.setSecurityMetadataSource(securityMetadataSource);
                        return o;
                    }
                })
                //登出
                .and().logout()
                .permitAll()
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                //登入
                .and().formLogin()
                .permitAll()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                //异常处理(权限拒绝、登录失效等)
                .and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                //会话管理
                .and().sessionManagement()
                .maximumSessions(1)
                .expiredSessionStrategy(sessionInformationExpiredStrategy);
    }
}