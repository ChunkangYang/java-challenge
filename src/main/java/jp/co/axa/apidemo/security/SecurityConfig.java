package jp.co.axa.apidemo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
	        .authorizeRequests()
	            .antMatchers("/api/v1/public").permitAll()
	            .antMatchers("/api/v1/**").authenticated()
	        .and()
	        .httpBasic();
    }
}