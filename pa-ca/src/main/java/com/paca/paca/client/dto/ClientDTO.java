package com.paca.paca.client.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private String identityDocument;
}
