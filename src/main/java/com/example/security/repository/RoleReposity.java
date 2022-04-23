package com.example.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.security.entity.Role;

public interface RoleReposity extends JpaRepository<Role, Long> {

}
