package com.neethu.springboot.springbootsite.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
	
	@Id @GeneratedValue
	private Long id;
	private String userName;
	private String password;
	private String [] roles;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String [] getRoles() {
		return roles;
	}
	public void setRoles(String [] roles) {
		this.roles = roles;
	}
	
	private User(){}
	
	public User(String userName, String password, String ... roles){
		this.userName = userName;
		this.password = password;
		this.roles = roles;
	}
}
