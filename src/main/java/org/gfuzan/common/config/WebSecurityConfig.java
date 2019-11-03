package org.gfuzan.common.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/testController/**").permitAll()
			.antMatchers("/admin","/admin/**").hasRole("ADMIN")
			.antMatchers("/user","/user/**").hasAnyRole("ADMIN","USER")
			.antMatchers("/test","/test/**").hasAnyRole("ADMIN","DEV")
			.anyRequest().permitAll().and()
			.formLogin().loginPage("/login").permitAll();
	}
	
    @Bean
    @Override
	public UserDetailsService userDetailsService() {
		JdbcUserDetailsManager userManager = new JdbcUserDetailsManager();
		userManager.setDataSource(dataSource);
//		
//		// 管理员帐户
//		userManager.createUser(User.withUsername("uadmin").password("padmin").roles("ADMIN").build());
//
//		// 用户帐户
//		userManager.createUser(User.withUsername("uuser").password("puser").roles("USER").build());
//		// 运维帐户
//		userManager.createUser(User.withUsername("udev").password("pdev").roles("DEV").build());

		return userManager;
	}
}