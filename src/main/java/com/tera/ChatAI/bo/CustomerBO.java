package com.tera.ChatAI.bo;

import com.neovisionaries.i18n.CountryCode;
import com.tera.ChatAI.entity.CustomerMerchantCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class CustomerBO {

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

    private Set<AccountBO> accounts;

    public BigDecimal avgBalance() {
        var totalAvgBalance = accounts.stream().map(AccountBO::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalAvgBalance.divide(new BigDecimal(accounts.size()), new MathContext(5));
    }

    public long totalInflow() {
        return accounts.stream()
                .map(AccountBO::totalAccountInflows)
                .reduce(Long::sum).orElse(0L);
    }

    public long totalOutflow() {
        return accounts.stream()
                .map(AccountBO::totalAccountOutflows)
                .reduce(Long::sum).orElse(0L);
    }

    public Set<CustomerMerchantCategory> merchantCategories() {
        var mapCustomerMasterDataMerchantCategories = accounts.stream()
                .map(AccountBO::accountMerchantCategories)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, BigDecimal::add));

        return mapCustomerMasterDataMerchantCategories.entrySet().stream()
                .map(entry -> CustomerMerchantCategory.builder()
                        .merchantType(entry.getKey())
                        .merchantAmount(entry.getValue())
                        .build())
                .collect(Collectors.toSet());
    }

}
