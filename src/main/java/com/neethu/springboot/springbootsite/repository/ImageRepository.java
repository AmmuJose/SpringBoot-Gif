package com.neethu.springboot.springbootsite.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.neethu.springboot.springbootsite.entity.Image;

public interface ImageRepository extends PagingAndSortingRepository<Image, Long>{
	
	public Image findByName(String name);
}
