package com.example.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.security.entity.AppUser;


public interface AppUserRepository extends JpaRepository<AppUser, String>{
	Optional<AppUser> findByUsername(String username);
}
