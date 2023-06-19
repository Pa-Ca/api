package com.paca.paca.auth.utils;

import java.util.List;
import java.util.Arrays;
import java.lang.reflect.Method;
import java.lang.annotation.Target;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.paca.paca.exception.exceptions.ForbiddenException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ValidateRolesInterceptor implements HandlerInterceptor {

    @Inherited
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.METHOD })
    public @interface ValidateRoles {
        String[] value();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ForbiddenException {
        Method method;
        try {
            method = ((HandlerMethod) handler).getMethod();
        } catch (Exception e) {
            return true;
        }
        ValidateRoles annotation = AnnotationUtils.findAnnotation(method, ValidateRoles.class);
        if (annotation != null) {
            List<String> allowedRoles = Arrays.asList(annotation.value());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!auth.getAuthorities().stream().anyMatch(a -> allowedRoles.contains(a.getAuthority()))
                    && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {
                throw new ForbiddenException("Unauthorized access for this operation");
            }
        }
        return true;
    }
}