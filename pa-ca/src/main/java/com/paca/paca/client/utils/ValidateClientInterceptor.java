package com.paca.paca.client.utils;

import java.util.Map;
import java.lang.reflect.Method;
import java.lang.annotation.Target;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.paca.paca.client.model.Client;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.exception.exceptions.ForbiddenException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ValidateClientInterceptor implements HandlerInterceptor {

    @Inherited
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.METHOD })
    public @interface ValidateClient {
        String idField() default "id";
    }

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ForbiddenException {
        Method method;
        try {
            method = ((HandlerMethod) handler).getMethod();
        } catch (Exception e) {
            return true;
        }
        ValidateClient annotation = AnnotationUtils.findAnnotation(method, ValidateClient.class);
        if (annotation != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {
                return true;
            }

            Client client = clientRepository.findByUserEmail(auth.getName()).get();
            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long clientId = Long.parseLong((String) pathVariables.get(annotation.idField()));
            if (!client.getId().equals(clientId)) {
                throw new ForbiddenException("Unauthorized access for this operation");
            }
        }
        return true;
    }
}