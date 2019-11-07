package org.gfuzan.common.security.config;

import javax.servlet.http.HttpServletRequest;

import org.gfuzan.common.security.service.CustomAuthenticationDetailsSource;
import org.gfuzan.common.security.service.CustomAuthenticationSuccessHandler;
import org.gfuzan.common.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/testController/**").permitAll()
			.antMatchers("/security/admin","/security/admin/**").hasRole("ADMIN")
			.antMatchers("/security/user","/security/user/**").hasAnyRole("ADMIN","USER")
			.antMatchers("/security/test","/security/test/**").hasAnyRole("ADMIN","DEV")
			.anyRequest().permitAll().and()
			.formLogin().loginPage("/security/login")
			.authenticationDetailsSource(createCustomAuthenticationDetailsSource())
			.defaultSuccessUrl("/security/home").successHandler(createAuthenticationSuccessHandler())
			.failureUrl("/error");
	}
	
    /**
     * 创建用户详情服务Bean
     * @return
     */
    @Bean
    @Override
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
    
    /**
     * 创建认证成功处理服务
     * @return
     */
    private AuthenticationSuccessHandler createAuthenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }
    /**
     * 创建认证认证源
     * @return
     */
    private AuthenticationDetailsSource<HttpServletRequest, ?> createCustomAuthenticationDetailsSource(){
        return new CustomAuthenticationDetailsSource();
    }
}