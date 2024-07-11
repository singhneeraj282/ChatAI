package com.tera.ChatAI.entity;

import com.neovisionaries.i18n.CurrencyCode;
import com.tera.ChatAI.dto.LedgerType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "TRANSACTION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "COUNTER_PARTY_NAME")
    private String counterPartyName;

    @Column(name = "COUNTER_PARTY_ACCOUNT")
    private String counterPartyAccount;

    @Column(name = "TYPE")
    private LedgerType type;

    @Column(name = "REMITTANCE_INFO")
    private String remittanceInfo;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "BOOKING_DATE")
    private Instant bookingDate;

    @Column(name = "VALUE_DATE")
    private Instant valueDate;

    @Column(name = "CURRENCY")
    private CurrencyCode currency;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

}
