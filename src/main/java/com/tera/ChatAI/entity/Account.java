package com.tera.ChatAI.entity;

import com.neovisionaries.i18n.CurrencyCode;
import com.tera.ChatAI.dto.AccountStatus;
import com.tera.ChatAI.dto.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "ACCOUNT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @Column(name = "BBAN")
    private long accountNumber;//BBAN (Account Number)

    @Column(name = "ACCOUNT_TYPE")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "IBAN_NUMBER")
    private String ibanNumber;//International bank account

    @Column(name = "IBAN_NAME")
    private String ibanName;

    @Column(name = "PAN")
    private String pan;

    @Column(name = "CURRENCY")
    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "BALANCE")
    private BigDecimal balance;

    @Column(name = "LAST_CHANGE")
    private Instant lastChange;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
    private List<Transaction> transactions;

}
