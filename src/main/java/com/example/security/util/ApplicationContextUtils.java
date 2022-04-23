package com.example.security.util;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtils {
	
	private static ApplicationContext context;
	
	public ApplicationContextUtils(ApplicationContext context) {
		this.context=context;
	}

	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> t) {
		return context.getBean(t);
	}
}
