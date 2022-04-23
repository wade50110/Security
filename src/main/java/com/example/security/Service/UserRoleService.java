package com.example.security.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.example.security.entity.UserRole;
import com.example.security.repository.AppUserRepository;
import com.example.security.repository.RoleReposity;
import com.example.security.repository.UserRoleReposity;

@Service
@DependsOn({"appUserService", "roleService"})
public class UserRoleService {

	@Autowired
    private  UserRoleReposity userRoleRepo;
	
//	@Autowired
//    private  AppUserRepository userRepo;
//	
//	@Autowired
//    private  RoleReposity roleRepo;

//    @PostConstruct
//    void init() {
//        List<AppUser> userList = userRepo.findAll();
//        List<Role> roleList = roleRepo.findAll();
//
//        List<UserRole> userRoleList = new ArrayList<>();
//        for (AppUser user : userList) {
//            for (Role role : roleList) {
//
//                String username = user.getUsername();
//                String roleName = role.getRoleName();
//
//                if (username.equals("david")
//                        && (roleName.equals("PRODUCT_MANAGER") || roleName.equals("HR_MANAGER"))) {
//                    setUserRoleList(userRoleList, user.getId(), role.getId());
//                } else if (username.equals("andy")
//                        && roleName.equals("PRODUCT_MANAGER")) {
//                    setUserRoleList(userRoleList, user.getId(), role.getId());
//                } else if (username.equals("amber")
//                        && roleName.equals("PRODUCT_STAFF")) {
//                    setUserRoleList(userRoleList, user.getId(), role.getId());
//                } else if (username.equals("bob")
//                        && roleName.equals("HR_MANAGER")) {
//                    setUserRoleList(userRoleList, user.getId(), role.getId());
//                } else if (username.equals("bill")
//                        && roleName.equals("HR_STAFF")) {
//                    setUserRoleList(userRoleList, user.getId(), role.getId());
//                } else if (username.equals("clare")
//                        && (roleName.equals("PRODUCT_STAFF") || roleName.equals("HR_STAFF"))) {
//                    setUserRoleList(userRoleList, user.getId(), role.getId());
//                }
//            }
//        }
//        userRoleRepo.saveAll(userRoleList);
//    }


    public List<UserRole> getByUserId(Long userId) {
        return userRoleRepo.findByUserId(userId);
    }

}
