package com.paca.paca.client.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Builder
@Getter
@Setter
public class ClientDTO {

    private Long id;
    private Long userId;
    private String email;
    private String name;
    private String surname;
    private String stripeCustomerId;
    private String phoneNumber;
    private String address;
    private Date dateOfBirth;
}
