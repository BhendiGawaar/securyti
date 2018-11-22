/**
 * 
 */
package com.example.securyti.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author vishal
 * Created 17-Nov-2018 8:05:00 PM
 */
public class RegistrationDto extends LoginDto 
{
	@NotBlank
    @Size(min = 4, max = 40)
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
