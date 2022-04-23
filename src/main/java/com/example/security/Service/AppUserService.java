package com.example.security.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.security.entity.AppUser;
import com.example.security.repository.AppUserRepository;

@Service
public class AppUserService {
	
	@Autowired
	private AppUserRepository repository;
	
	
//	@PostConstruct	
//    void init() {
//        List<AppUser> userList = List.of(
//        		AppUser.builder()
//                        .username("david") // PRODUCT_MANAGER, HR_MANAGER
//                        .password(BCrypt.hashpw("123", BCrypt.gensalt(10))).build(),
//                AppUser.builder()
//                        .username("andy")  // PRODUCT_MANAGER
//                        .password(BCrypt.hashpw("123", BCrypt.gensalt(10))).build(),
//                AppUser.builder()
//                        .username("amber") // PRODUCT_STAFF
//                        .password(BCrypt.hashpw("123", BCrypt.gensalt(10))).build(),
//                AppUser.builder()
//                        .username("bob")   // HR_MANAGER
//                        .password(BCrypt.hashpw("123", BCrypt.gensalt(10))).build(),
//                AppUser.builder()
//                        .username("bill")  // HR_STAFF
//                        .password(BCrypt.hashpw("123", BCrypt.gensalt(10))).build(),
//                AppUser.builder()
//                        .username("clare") // PRODUCT_STAFF, HR_STAFF
//                        .password(BCrypt.hashpw("123", BCrypt.gensalt(10))).build());
//
//        repository.saveAll(userList);
//    }
	
	public List<String> getAllUserNames() {
        return repository.findAll().stream()
                .map(AppUser::getUsername)
                .collect(Collectors.toList());
    }

    public Optional<AppUser> getByUsername(String username) {
        return repository.findByUsername(username);
    }
    
}
