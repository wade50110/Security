package com.example.security.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.security.entity.AppUser;
import com.example.security.entity.AuthRequest;
import com.example.security.entity.Role;
import com.example.security.util.ApplicationContextUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
	
	@Autowired
	private static ApplicationContext context;
	
	@Autowired
	private  AppUserService userDao;

	@Autowired
    private  RoleService roleDao;
	
	@Autowired
    private  UserRoleService userRoleDao;

	 private final String KEY = "VincentIsRunningBlogForProgrammingBeginner";

	 public String generateToken(AuthRequest request) {
		 	AuthenticationManager authenticationManager = (AuthenticationManager) ApplicationContextUtils.getBean(AuthenticationManager.class);
	        Authentication authentication =
	                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
	        authentication = authenticationManager.authenticate(authentication);
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

	        Calendar calendar = Calendar.getInstance();
	        calendar.add(Calendar.MINUTE, 10);

	        Claims claims = Jwts.claims();
	        claims.put("username", userDetails.getUsername());
	        claims.setExpiration(calendar.getTime());
	        claims.setIssuer("Programming Classroom");

	        Key secretKey = Keys.hmacShaKeyFor(KEY.getBytes());

	        return Jwts.builder()
	                .setClaims(claims)
	                .signWith(secretKey)
	                .compact();
	 }
	 
	 public Map<String, Object> parseToken(String token) {
	        Key secretKey = Keys.hmacShaKeyFor(KEY.getBytes());

	        JwtParser parser = Jwts.parserBuilder()
	                .setSigningKey(secretKey)
	                .build();

	        Claims claims = parser
	                .parseClaimsJws(token)
	                .getBody();
	        
	        
	        AppUser user = userDao.getByUsername(claims.get("username").toString()).orElse(new AppUser());
	        
	        HashMap<String, Object> resultMap = new HashMap<String, Object>();
	        
	        resultMap = (HashMap<String, Object>)claims.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	        
	        resultMap.put("user_type", getRoles(user.getId()));

	        return resultMap;
	 }
	 
	 private String[] getRoles(Long userId) {
	        return userRoleDao.getByUserId(userId).stream()
	                .map(e -> roleDao.getById(e.getRoleId())
	                        .map(Role::getRoleName).orElse(null))
	                .toArray(String[]::new);
	    }
}
