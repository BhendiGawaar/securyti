package com.example.securyti.security.jwt;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.securyti.config.ConfigurationService;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private ConfigurationService configService;
	private JwtTokenService jwtTokenService;

    public JwtAuthorizationFilter(AuthenticationManager authManager, ConfigurationService configService, 
    		final JwtTokenService jwtTokenService) {
        super(authManager);
        this.configService = configService;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(configService.getHeaderField());

        if (header == null || !header.startsWith(configService.getTokenPrefix())) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(configService.getHeaderField());
        if (token != null) {
            // parse the token.
            String user = jwtTokenService.getUsernameFromJWT(token);

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
