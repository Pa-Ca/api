package com.paca.paca.reservation.utils;

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
import com.paca.paca.business.model.Business;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.reservation.repository.ClientGroupRepository;
import com.paca.paca.reservation.repository.ReservationRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ValidateReservationOwnerInterceptor implements HandlerInterceptor {

    @Inherited
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.METHOD })
    public @interface ValidateReservationOwner {
        boolean isClientOwner() default true;
    }

    @Autowired
    private ClientGroupRepository clientGroupRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ForbiddenException {
        Method method = ((HandlerMethod) handler).getMethod();
        ValidateReservationOwner annotation = AnnotationUtils.findAnnotation(method, ValidateReservationOwner.class);
        if (annotation != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);  
            Long reservationId = Long.parseLong((String) pathVariables.get("id"));

            if (annotation.isClientOwner()) {
                Client client = clientRepository.findByUserEmail(auth.getName()).get();
                if (clientGroupRepository.existsByReservationIdAndClientId(reservationId, client.getId())) {
                    throw new ForbiddenException("Unauthorized access for this operation");
                }
            }
            else {
                Business business = businessRepository.findByUserEmail(auth.getName()).get();
                if (reservationRepository.existsByIdAndBranch_Business_Id(reservationId, business.getId())) {
                    throw new ForbiddenException("Unauthorized access for this operation");
                }
            }
        }
        return true;
    }
}
