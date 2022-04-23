package com.example.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.security.Service.JWTService;
import com.example.security.entity.AuthRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

//這個 Handler 就是專門處理當登入成功後，可以採取什麼動作。同時也可以利用 Authentication 物件取得 account、authority。
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
	//這個 handler 可以做兩件事情：
	//一、我們需要 session 去存放 account、authority，以便之後如何判斷過來的 request。這並不符合 Restful 的形式，
	//所以這邊是採用混合的方式。因此在這邊也可以透過 request 去取得 session，再將 account、authority，去放置在 session 的 attribute 裡面。
	//二、再來這邊可以定義成功登入之後要回復的 json 格式，那因為是採前後端分離的格式，也就是說前端需要身分的訊息，
	//它才能控制之後要呈現的頁面是給哪個身分的角色去看，所以我們可以回傳角色身分的資訊。
    private final String LOGGED_IN = "logged_in";
    private final String USER_TYPE = "user_type";
    
    private JWTService jwt = new JWTService();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
    	User user = (User)authentication.getPrincipal();
        Collection collection = authentication.getAuthorities();
        String authority = collection.iterator().next().toString();
        HttpSession session = req.getSession();
        session.setAttribute(LOGGED_IN, user.getUsername());
        session.setAttribute(USER_TYPE, authority);
        AuthRequest auth = new AuthRequest(user.getUsername(),"123");
        Map<String, String> result = new HashMap<>();
        result.put("authority", authority);
        result.put("token", jwt.generateToken(auth));
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        resp.setStatus(200);
        ObjectMapper om = new ObjectMapper();
        out.write(om.writeValueAsString(result));
        out.flush();
        out.close();
    }
}

