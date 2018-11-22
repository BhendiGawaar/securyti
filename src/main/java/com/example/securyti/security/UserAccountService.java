package com.example.securyti.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.securyti.entity.User;
import com.example.securyti.repository.UserRepository;

@Component
public class UserAccountService implements UserDetailsService{

	private UserRepository userRepository;
	
	
	public UserAccountService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username); 
		if(user == null){
			new UsernameNotFoundException("User not found with username or email : " + username);
		}
		return new UserAccount(user.getUsername(), user.getPassword());
	}

}
