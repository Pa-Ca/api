package com.paca.paca.business.utils;
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
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.exception.exceptions.ForbiddenException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class ValidateBusinessInterceptor implements HandlerInterceptor {

    @Inherited
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.METHOD })
    public @interface ValidateBusiness {
        String idField() default "id";
    }

    @Autowired
    private BusinessRepository businessRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ForbiddenException {
        Method method = ((HandlerMethod) handler).getMethod();
        com.paca.paca.business.utils.ValidateBusinessInterceptor.ValidateBusiness annotation = AnnotationUtils.findAnnotation(method, com.paca.paca.business.utils.ValidateBusinessInterceptor.ValidateBusiness.class);
        if (annotation != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {
                return true;
            }

            Business business = businessRepository.findByUserEmail(auth.getName()).get();
            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long businessId = Long.parseLong((String) pathVariables.get(annotation.idField()));
            if (!business.getId().equals(businessId)) {
                throw new ForbiddenException("Unauthorized access for this operation");
            }
        }
        return true;
    }
}