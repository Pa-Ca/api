package com.paca.paca.client.model;

import java.util.Date;

import com.paca.paca.user.model.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"client\"")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @SequenceGenerator(name = "client_seq", sequenceName = "client_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "identity_document", nullable = false)
    private String identityDocument;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "stripe_customer_id", nullable = false)
    private String stripeCustomerId;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
}
