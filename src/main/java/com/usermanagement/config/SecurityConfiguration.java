package com.usermanagement.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
		.disable()
		.authorizeRequests()
		.antMatchers("/api/login", "/api/signup", "/api/activateaccount","/api/swagger-ui.html**")
		.permitAll()
		.antMatchers(HttpMethod.OPTIONS, "/api/**")
		.permitAll()// allow CORS option calls
		.anyRequest()
		.authenticated()
		.and()
		.logout()
		.logoutUrl("/api/logout")
		.logoutSuccessUrl("/api/login")
		.deleteCookies("JSESSIONID")
		.invalidateHttpSession(true);
		http.httpBasic();
		
		// add below if you don't want session creation
		//http.sessionManagement()
		//.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		 
	}
	
	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
}
