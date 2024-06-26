package com.jeipz.pma.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;
	
	@Autowired
	BCryptPasswordEncoder bCryptEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.usersByUsernameQuery("SELECT username, password, enabled " +
				"FROM user_accounts WHERE username = ?")
			.authoritiesByUsernameQuery("SELECT username, role " +
				"FROM user_accounts WHERE username = ?")
			.dataSource(dataSource)
			.passwordEncoder(bCryptEncoder);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/projects/new").hasAuthority("ADMIN")
			.antMatchers("/projects/save").hasAuthority("ADMIN")
			.antMatchers("/employees/new").hasAuthority("ADMIN")
			.antMatchers("/employees/save").hasAuthority("ADMIN")
			.antMatchers("/", "/**").permitAll()
			.and()
			.formLogin();
		
		http.csrf().disable();
	}
	
}
