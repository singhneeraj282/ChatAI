package com.tera.ChatAI.entity;

import com.neovisionaries.i18n.CountryCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "CUSTOMER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "user_name", unique = true)
    private String username;

    @Column(name = "password")
    private String password;
    //may be separate address entity as user can have multiple address
    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    @Enumerated(EnumType.STRING)
    private CountryCode country;

    @Column(name = "customer_type")
    private String customerType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "customer")//To make this association bidirectional, all we’ll have to do is to define the referencing side. The inverse or the referencing side simply maps to the owning side.
    private Set<Account> account;

}
