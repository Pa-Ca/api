package com.paca.paca.business;

import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BusinessServiceTest {

    @Mock
    private UserRepository userRepository;
    // private AutoCloseable autoCloseable;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        //underTest = new UserService(userRepository);
    }

    // @Test
    // @Disabled
    // void getAll() {
    //     // when
    //     underTest.getAll();

    //     // then
    //     verify(userRepository).findAll();
    // }
}