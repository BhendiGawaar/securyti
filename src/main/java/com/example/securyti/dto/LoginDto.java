/**
 * 
 */
package com.example.securyti.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author vishal
 * Created 17-Nov-2018 8:02:23 PM
 */
public class LoginDto 
{

	@NotBlank
	@Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    
	public LoginDto(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	
	public LoginDto() {
		super();
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
