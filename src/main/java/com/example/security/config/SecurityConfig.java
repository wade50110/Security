package com.example.security.config;

import java.io.PrintWriter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.security.Filter.LoginAuthenticationFilter;
import com.example.security.handler.AccessDeniedHandlerImpl;
import com.example.security.handler.AuthenticationFailureHandlerImpl;
import com.example.security.handler.AuthenticationSuccessHandlerImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableWebSecurity //Enable springFliterChain(用來啟用基於annotation註解如@Security， @PreAuthorize，@RolesAllowed的服務層安全機制)
@EnableGlobalMethodSecurity(jsr250Enabled=true) //https://www.cnblogs.com/520playboy/p/7286085.html  默認是禁用的
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
    private UserDetailsService userDetailsService;
	
	
	// 指定密码的加解密方式
    @Bean
    PasswordEncoder passwordEncoder(){
        // 使用BCrypt强哈希函数加密方案，密钥迭代次数设为10（默认即为10）
        return new BCryptPasswordEncoder(10);
    }
	
//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//		http
//        .authorizeRequests()
//        .antMatchers(HttpMethod.GET, "/users/**").authenticated()
//        .antMatchers(HttpMethod.GET).permitAll()
//        .antMatchers(HttpMethod.POST, "/users").permitAll()
//        .anyRequest().authenticated()//對剩下的 API 定義規則，所以才放在最後一個。此處規定只有通過驗證的呼叫方才可存取，否則會收到 HTTP 403（Forbidden）的狀態碼。
//        .and()
//        .csrf().disable() //關閉對 CSRF（跨站請求偽造）攻擊的防護。這樣 Security 機制才不會拒絕外部直接對 API 發出的請求，如 Postman 與前端
//        .formLogin();//啟用內建的登入畫面。雖然在現今前後端分離的趨勢下，我們不再使用這種登入畫面，但筆者會在下一節用來進行身份驗證，幫助讀者體會
//    }
	
	//建立 LoginAuthenticationFilter 的 bean，讓 Spring Security 可以採用
	//最後最重要的是我們要設定該 filter 是專門 for 哪個路由，也就是這邊我們可以自定義 login 的路由
	//，而不是採用傳統的 Spring Security 提供的表單路由。
	@Bean
	LoginAuthenticationFilter loginAuthenticationFilter() throws Exception {
	    LoginAuthenticationFilter filter = new LoginAuthenticationFilter();
	    filter.setAuthenticationManager(authenticationManagerBean());
	    filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandlerImpl());
	    filter.setAuthenticationFailureHandler(new AuthenticationFailureHandlerImpl());
	    filter.setFilterProcessesUrl("/api/login"); //自定義 login 的路由
	    return filter;
	}
	
	
	//至於下面的 AuthenticationManager，因為 filter 需要有 AuthenticationManager 的設定，但是用原本 Spring Security 的即可。因為沒設定的話
	//，會報錯誤，而且給的錯誤訊息就是需要去設定 AuthenticationManager。
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}
	
	
	//這個 configure，就是將剛剛我們寫好的組件加入進去設定裡面
	//從一開始的 exceptionHanling 可以拿來設定 authenticationEntryPoint、accessDeniedHandler
	//，我們只要換成我們定義的RestAuthenticationEntryPoint、AccessDeniedHandlerImpl 即可。
	//接著是新增 filter，也就是把我們的 loginAuthenticationFilter 跟原始的 UsernamePasswordAuthenticationFilter 去做替換，才能實現我們想要的功能。
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.exceptionHandling()  //從一開始的 exceptionHanling 可以拿來設定 authenticationEntryPoint、accessDeniedHandler
	        .authenticationEntryPoint(new RestAuthenticationEntryPoint()) //我們只要換成我們定義的RestAuthenticationEntryPoint
	        .accessDeniedHandler(new AccessDeniedHandlerImpl()) //我們只要換成我們定義的AccessDeniedHandlerImpl
	        .and() //接著
	        //新增 filter也就是把我們的 loginAuthenticationFilter 跟原始的 UsernamePasswordAuthenticationFilter 去做替換
	        .addFilterAt(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) 
	        .authorizeRequests()  //這個方式，就是設定每個 Requests 都必須經過身分驗證的手續
	        .antMatchers(HttpMethod.GET, "/**").authenticated()
	        .antMatchers(HttpMethod.POST, "/**").authenticated()
	        .anyRequest().authenticated()
//	        .antMatchers("/api/admin/**").hasRole("ADMIN") // 【/api/admin/】 路徑下，都只能由 hasRole (“ADMIN”)
//	        .antMatchers("/api/user/**").hasRole("USER") // 【/api/user/】 路徑下，都只能由 hasRole (“USER”)
	        .and()
	        .logout()
	        .logoutUrl("/api/logout") //定義登出的路由
	        .invalidateHttpSession(true) //幫我們主動將 session destory 掉
	        .logoutSuccessHandler((req, resp, auth) -> {  //還有可以添加登出成功的 handler，這邊也就是能自訂我們想要的 json 回復訊息
	            resp.setContentType("application/json;charset=UTF-8");
	            PrintWriter out = resp.getWriter();
	            resp.setStatus(200);
	            Map<String, String> result = Map.of("message", "登出成功");
	            ObjectMapper om = new ObjectMapper();
	            out.write(om.writeValueAsString(result));
	            out.flush();
	            out.close();
	        })
	        .and()
	        .csrf().disable(); ////關閉對 CSRF（跨站請求偽造）攻擊的防護。這樣 Security 機制才不會拒絕外部直接對 API 發出的請求，如 Postman 與前端
	}
}
