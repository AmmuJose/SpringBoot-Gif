package com.neethu.springboot.springbootsite.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.neethu.springboot.springbootsite.entity.Image;
import com.neethu.springboot.springbootsite.entity.User;
import com.neethu.springboot.springbootsite.repository.ImageRepository;
import com.neethu.springboot.springbootsite.repository.UserRepository;

@Service
public class ImageService {
	private static String UPLOAD_ROOT = "upload-dir";
	private final ImageRepository imageRepo;
	private final ResourceLoader resourceLoder;
	private final SimpMessagingTemplate messagingTemplate;
	
	
	@Autowired
	public ImageService(ImageRepository imageRepo, ResourceLoader resourceLoder, SimpMessagingTemplate messagingTemplate) {
		//super();
		this.imageRepo = imageRepo;
		this.resourceLoder = resourceLoder;
		this.messagingTemplate = messagingTemplate;
	}
	
	public Resource findOneImage(String fileName){
		return resourceLoder.getResource("file:" + UPLOAD_ROOT + "/" + fileName);
	}
	
	public Page<Image> findPage(Pageable pageable){
		return imageRepo.findAll(pageable);
	}
	
	public void createImage(MultipartFile file) throws IOException{
		if(!file.isEmpty()){
			Files.copy(file.getInputStream(), Paths.get(UPLOAD_ROOT, file.getOriginalFilename()));
			imageRepo.save(new Image(file.getOriginalFilename()));
			messagingTemplate.convertAndSend("/topic/newImage", file.getOriginalFilename());
		}
	}
	
	public void deleteImage(String fileName) throws IOException{
		final Image byName = imageRepo.findByName(fileName);
		imageRepo.delete(byName);
		Files.deleteIfExists(Paths.get(UPLOAD_ROOT, fileName));
		messagingTemplate.convertAndSend("/topic/deleteImage", fileName);
	}
	
	@Bean
	//@Profile("dev")
	CommandLineRunner setUp(ImageRepository repo, UserRepository userRepo) throws IOException{
		
		String password = "123";
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPassword = encoder.encode(password);
		
		return (args) -> {
			
			User admin = userRepo.save(new User("admin", hashedPassword, "ROLE_ADMIN", "ROLE_USER"));
			User user = userRepo.save(new User("user", hashedPassword, "ROLE_USER"));
			
			FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));
			Files.createDirectory(Paths.get(UPLOAD_ROOT));
			FileCopyUtils.copy("Test file", new FileWriter(UPLOAD_ROOT + "/test"));
			repo.save(new Image("test"));
			
			FileCopyUtils.copy("Test file2", new FileWriter(UPLOAD_ROOT + "/test2"));
			repo.save(new Image("test2"));
			
			FileCopyUtils.copy("Test file3", new FileWriter(UPLOAD_ROOT + "/test3"));
			repo.save(new Image("test3"));			
		};
	}
}
