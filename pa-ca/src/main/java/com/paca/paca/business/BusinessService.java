package com.paca.paca.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NoContentException;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessService {
    
    private final BusinessRepository businessRepository;

    // @Autowired
    // public UserService(UserRepository userRepository) {
    //     this.userRepository = userRepository;
    // }

    public BusinessService(BusinessRepository userRepository) {
        this.businessRepository = userRepository;
    }

    public List<Business> getAll() {
        return businessRepository.findAll();
    }

    public Business getById(Long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new NoContentException("Business with id: " + id + " does not exists"));
    }

}