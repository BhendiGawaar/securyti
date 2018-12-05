package com.example.securyti.security.jwt;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import com.example.securyti.security.UserAccount;
import com.example.securyti.security.UserAccountService;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	private final AuthenticationManager authenticationManager;
	  private final JwtTokenService jwtTokenService;
	  private final BCryptPasswordEncoder passwordEncoder;
	  private final UserAccountService userAccountService;

	  public JwtAuthenticationFilter(
	      final AuthenticationManager authenticationManager,
	      final JwtTokenService jwtTokenService,
	      final BCryptPasswordEncoder passwordEncoder,
	      final UserAccountService userAccountService) {
	    this.authenticationManager = authenticationManager;
	    this.jwtTokenService = jwtTokenService;
	    this.passwordEncoder = passwordEncoder;
	    this.userAccountService = userAccountService;
	  }

	  @Override
	  public Authentication attemptAuthentication(final HttpServletRequest req,
	      final HttpServletResponse res) {
			
	    String jwt = jwtTokenService.getTokenFromRequest(req);
	    UserAccount userAccount = null;
	    
	    if (StringUtils.hasText(jwt) && jwtTokenService.validateToken(jwt)) {
		    userAccount = (UserAccount) userAccountService.loadUserByUsername(jwtTokenService.getUsernameFromJWT(jwt));
		      		      		        		       
	    }
	    
	    if(userAccount == null){
	    	throw new BadCredentialsException("Bad credentials");
	    }
	    AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userAccount.getUsername(),
	            userAccount.getPassword(), Collections.emptyList());
	    Authentication auth = authenticationManager.authenticate(authToken);
	    return auth;
	  }

	  private String getUsername(final UserAccount creds) {
	    if (creds != null) {
	      return creds.getUsername();
	    }
	    return null;
	  }



	  @Override
	  protected void successfulAuthentication(final HttpServletRequest req,
	      final HttpServletResponse res, final FilterChain chain,
	      final Authentication auth) throws IOException, ServletException {

	    final UserAccount account = (UserAccount) auth.getPrincipal();
	    jwtTokenService.addTokenToResponse(account, res);

	    super.successfulAuthentication(req, res, chain, auth);
	  }


}
