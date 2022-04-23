package com.example.security.controller;

import java.util.Map;

import javax.annotation.security.RolesAllowed;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

    @GetMapping("/admin/hello")
    @RolesAllowed({"PRODUCT_MANAGER"})
    public Map<String, String> adminSayHello() {
        Map<String, String> result = Map.of("message", "admin say hello");
        return result;
    }

    @GetMapping("/user/hello")
    @RolesAllowed({"HR_STAFF"})
    public Map<String, String> userSayHello() {
        Map<String, String> result = Map.of("message", "user say hello");
        return result;
    }
}
