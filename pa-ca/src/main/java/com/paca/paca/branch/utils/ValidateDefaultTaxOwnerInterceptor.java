package com.paca.paca.branch.utils;

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

import com.paca.paca.business.model.Business;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.exception.exceptions.ForbiddenException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ValidateDefaultTaxOwnerInterceptor implements HandlerInterceptor {

    @Inherited
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.METHOD })
    public @interface ValidateDefaultTaxOwner {
    }

    @Autowired
    private DefaultTaxRepository defaultTaxRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ForbiddenException {
        Method method;
        try {
            method = ((HandlerMethod) handler).getMethod();
        } catch (Exception e) {
            return true;
        }
        ValidateDefaultTaxOwner annotation = AnnotationUtils.findAnnotation(method, ValidateDefaultTaxOwner.class);
        if (annotation != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {
                return true;
            }

            Business business = businessRepository.findByUserEmail(auth.getName()).get();
            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long defaultTaxId = Long.parseLong((String) pathVariables.get("id"));

            if (!defaultTaxRepository.existsByIdAndBranch_Business_Id(defaultTaxId, business.getId())) {
                throw new ForbiddenException("Unauthorized access for this operation");
            }
        }
        return true;
    }
}
