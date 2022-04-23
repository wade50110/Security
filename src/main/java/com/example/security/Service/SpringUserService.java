package com.example.security.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.security.entity.AppUser;
import com.example.security.entity.Role;

@Service
public class SpringUserService implements UserDetailsService {
	
	@Autowired
	private  AppUserService userDao;
	
	@Autowired
    private  UserRoleService userRoleDao;
	
	@Autowired
    private  RoleService roleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	AppUser user = userDao.getByUsername(username).orElse(new AppUser());
    	System.out.println("loadUserByUsername:" + username);
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(getRoles(user.getId()))
                .build();

        return userDetails;
    }
    
    private String[] getRoles(Long userId) {
        return userRoleDao.getByUserId(userId).stream()
                .map(e -> roleDao.getById(e.getRoleId())
                        .map(Role::getRoleName).orElse(null))
                .toArray(String[]::new);
    }

    public List<String> getAllUserNames() {
        return userDao.getAllUserNames();
    }

    public List<String> getAllRoleNames() {
        return roleDao.getAllRoleNames();
    }
}
