package com.paca.paca.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.paca.paca.auth.utils.ValidateRolesInterceptor;

@Configuration
public class SpringMVCConfig implements WebMvcConfigurer {

    @Autowired
    ValidateRolesInterceptor validateRolesInterceptor;

    @Override 
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(validateRolesInterceptor);
    }
    
}
