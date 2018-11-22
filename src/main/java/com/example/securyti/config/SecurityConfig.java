package com.example.securyti.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.example.securyti.security.UserAccountService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	protected UserAccountService userAccountService;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()                
                .anyRequest().authenticated();
	}
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	  @Override
	  protected void configure(final AuthenticationManagerBuilder auth)
	      throws Exception {
	    auth.userDetailsService(userAccountService);
	  }
}
