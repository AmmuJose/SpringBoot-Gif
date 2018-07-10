package com.neethu.springboot.springbootsite.repository;

import org.springframework.data.repository.CrudRepository;

import com.neethu.springboot.springbootsite.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{

		User findByUserName(String userName);
}
