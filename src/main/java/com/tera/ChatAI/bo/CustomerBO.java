package com.tera.ChatAI.bo;

import com.neovisionaries.i18n.CountryCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class CustomerBO {
    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String username;

    private String password;
    //may be separate address entity as user can have multiple address
    private String address;

    private String city;

    private CountryCode country;

    private String customerType;

    private Set<AccountBO> account;

    private long version;
}
