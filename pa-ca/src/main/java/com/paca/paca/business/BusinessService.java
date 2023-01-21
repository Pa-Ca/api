package com.paca.paca.business;

import org.springframework.stereotype.Service;

import com.paca.paca.exception.NoContentException;

// import java.net.http.HttpResponse;
import java.util.List;

@Service
public class BusinessService {
    
    private final BusinessRepository businessRepository;

    public BusinessService(BusinessRepository userRepository) {
        this.businessRepository = userRepository;
    }

    public List<Business> getAll() {
        return businessRepository.findAll();
    }

    public Business getBusinessById(Long id) {
        return businessRepository.findById(id).orElseThrow(
            () -> new NoContentException(
                "User with id: ${id} does not exists"
            )
        );
    }

}