package com.example.securyti.security.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.securyti.config.ConfigurationService;
import com.example.securyti.security.UserAccount;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtTokenService {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);
	
	private ConfigurationService configurationService;

	public JwtTokenService(final ConfigurationService configurationService) {
		super();
		this.configurationService = configurationService;
	}

	String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(configurationService.getHeaderField());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(configurationService.getTokenPrefix())) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

	public void addTokenToResponse(UserAccount account, HttpServletResponse res) {
				
		LocalDateTime expiry = LocalDateTime.now().plusSeconds(configurationService.getJwtExpirationInSec());
        
		String token = Jwts.builder()
                .setSubject(account.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(expiry.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, configurationService.getJwtSecret())
                .compact();
		res.addHeader(configurationService.getHeaderField(), configurationService.getTokenPrefix() + token);
	}
	
	public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(configurationService.getJwtSecret())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
	
	public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(configurationService.getJwtSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
