package com.tera.ChatAI.dto;

import com.neovisionaries.i18n.CountryCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
public class CustomerDTO {

    private long id;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String email;

    private String phone;

    private String username;

    private String password;
    //may be separate address entity as user can have multiple address
    private String address;

    private String city;

    private CountryCode country;

    private String customerType;

    private Set<AccountDTO> account;
}
