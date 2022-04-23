package com.example.security.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * extends AbstractSecurityWebApplicationInitializer等同
 * <filter>
	<filter-name>springSecurityFilterChain</filter-name>
	<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
	</filter>
	
	<filter-mapping>
		<filter-name>springSecurityFilterChain</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
	
	當任何request時o.s.web.filter.DelegatingFilterProxy呼叫SpringSecurityFilterChain
 */
public class WebSecurityConfig extends AbstractSecurityWebApplicationInitializer {
	
}	
