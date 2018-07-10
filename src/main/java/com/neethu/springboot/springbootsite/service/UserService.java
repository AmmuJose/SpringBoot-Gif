package com.neethu.springboot.springbootsite.service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.neethu.springboot.springbootsite.entity.User;
import com.neethu.springboot.springbootsite.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
	
	private final UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {		
		User user = userRepo.findByUserName(userName);
		return new org.springframework.security.core.userdetails.User(
				user.getUserName(), user.getPassword(), 
				Stream.of(user.getRoles())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList())
				);
	}
	
	@Autowired
	public UserService(UserRepository userRepo){
		this.userRepo = userRepo;
	}

}
