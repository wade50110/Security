package com.example.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.security.entity.UserRole;

public interface UserRoleReposity extends JpaRepository<UserRole, Long>{
	List<UserRole> findByUserId(Long userId);
}
