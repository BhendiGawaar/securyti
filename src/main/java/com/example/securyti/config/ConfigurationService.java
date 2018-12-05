package com.example.securyti.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

	@Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationInSec}")
    private int jwtExpirationInSec;
    
	@Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

	@Value("${jwt.headerField}")
    private String headerField;
    
    public String getJwtSecret() {
		return jwtSecret;
	}

	public int getJwtExpirationInSec() {
		return jwtExpirationInSec;
	}

	public String getTokenPrefix() {
		return tokenPrefix;
	}

	public String getHeaderField() {
		return headerField;
	}

    
}
