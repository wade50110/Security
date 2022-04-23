package com.example.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.Service.AppUserService;
import com.example.security.entity.AppUser;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUserController {

    @Autowired
    private AppUserService service;

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUser(@PathVariable("id") String id) {
    	System.out.println("getUser");
        AppUser user = new AppUser();
        return ResponseEntity.ok(user);
    }

}
