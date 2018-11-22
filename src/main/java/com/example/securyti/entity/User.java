package com.example.securyti.entity;

/**
 * @author vishal
 * Created 17-Nov-2018 3:50:23 PM
 */

import org.hibernate.annotations.NaturalId;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public User(String name, String username, String password) {
		super();
		this.name = name;
		this.username = username;
		this.password = password;
	}


	@NotBlank
    @Size(max = 40)
    private String name;

    @NaturalId
    @NotBlank
    @Size(max = 15)
    private String username;
   

    @NotBlank
    @Size(max = 100)
    private String password;


    public User() {

    }


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
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


	@Override
	public String toString() {
		return "User [name=" + name + ", username=" + username + ", password=" + password + "]";
	}
}
