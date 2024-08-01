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
    @Column(name = "account_id")
    private long accountId;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "iban_number")
    private String ibanNumber;//International bank account

    @Column(name = "iban_name")
    private String ibanName;

    @Column(name = "pan")
    private String pan;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "fee")
    private BigDecimal fee;

    @Column(name = "min_balance")
    private BigDecimal minBalance;

    @Column(name = "avg_balance")
    private BigDecimal avg_balance;

    @Column(name = "last_change")
    private Instant lastChange;

    @Version
    @Column(name = "version")
    private long version;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_account_id", referencedColumnName = "account_id")
    private List<Transaction> transactions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_account_id", referencedColumnName = "account_id")
    private List<CreditCard> creditCards;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_account_id", referencedColumnName = "account_id")
    private List<DebitCard> debitCards;


}
