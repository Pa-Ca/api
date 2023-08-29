package com.paca.paca.client.dto;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    private Long id;
    private Long userId;
    private String email;
    private String name;
    private String surname;
    private String identityDocument;
    private String address;
    private String phoneNumber;
    private String stripeCustomerId;
    private Date dateOfBirth;
}
