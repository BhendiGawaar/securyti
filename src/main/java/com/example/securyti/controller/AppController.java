package com.example.securyti.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.securyti.dto.ApiResponse;
import com.example.securyti.dto.LoginDto;
import com.example.securyti.dto.RegistrationDto;
import com.example.securyti.entity.User;
import com.example.securyti.repository.UserRepository;
import com.example.securyti.security.UserAccount;

@RestController
public class AppController {
	
	Logger logger = LoggerFactory.getLogger(AppController.class);
	private UserRepository userRepository;
	private AuthenticationManager authenticationManager;
	private PasswordEncoder passwordEncoder;
	
	public AppController(UserRepository userRepository, AuthenticationManager authenticationManager, 
			PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegistrationDto	dto){
		
		if(userRepository.existsByUsername(dto.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
		
		User user = new User(dto.getName(),dto.getUsername(),passwordEncoder.encode(dto.getPassword()));
		userRepository.save(user);
		return new ResponseEntity(new ApiResponse(true, "User registered successfully"),
                HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/login")
	public String login(@RequestBody LoginDto dto){
		Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		dto.getUsername(),
                		dto.getPassword()
                )
        );
		SecurityContextHolder.getContext().setAuthentication(authentication);
		logger.debug(authentication.getPrincipal().toString());
		return "done";
	}
	
	@GetMapping("/hello")
	public String hello(@RequestBody LoginDto dto){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return "empty";
        }

        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        
		User user = userRepository.findByUsername(dto.getUsername());
		logger.debug(user.toString());
		return "done";
	}
}
