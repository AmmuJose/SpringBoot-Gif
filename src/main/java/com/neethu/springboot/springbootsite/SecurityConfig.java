package com.neethu.springboot.springbootsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.neethu.springboot.springbootsite.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		http
		.csrf().disable()
		.headers().frameOptions().disable()
		.and()
		.authorizeRequests()		
			.anyRequest()
			.authenticated()
			.and()
		.formLogin()
			.permitAll();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Autowired
	public void configInJpaBasedUsers(AuthenticationManagerBuilder auth, UserService userSvc) throws Exception{
		auth.userDetailsService(userSvc).passwordEncoder(passwordEncoder());
	}	
	
}
