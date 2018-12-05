package com.example.securyti.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.securyti.security.UserAccountService;
import com.example.securyti.security.jwt.JwtAuthenticationFilter;
import com.example.securyti.security.jwt.JwtAuthorizationFilter;
import com.example.securyti.security.jwt.JwtTokenService;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	protected JwtAuthenticationFilter jwtAuthenticationFilter;
	protected JwtAuthorizationFilter JwtAuthorizationFilter;
	
	@Autowired
	protected UserAccountService userAccountService;
	
	@Autowired
	protected JwtTokenService jwtTokenService;
	
	@Autowired
	protected ConfigurationService configService; 
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
		final AuthenticationManager authenticationManager = authenticationManager();
	    jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager,
	        jwtTokenService, (BCryptPasswordEncoder) passwordEncoder(), userAccountService);
	    
	    JwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager, 
	    		configService, jwtTokenService);
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers("/register")
                .permitAll()                
                .anyRequest().authenticated().and().addFilter(jwtAuthenticationFilter).addFilter(JwtAuthorizationFilter);
	}
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Override
	protected void configure(final AuthenticationManagerBuilder auth)
	      throws Exception {
	    auth.userDetailsService(userAccountService).passwordEncoder(passwordEncoder());
	}
}
