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

    @Column(name = "counter_party_name")
    private String counterPartyName;

    @Column(name = "counter_party_account")
    private String counterPartyAccount;

    @Column(name = "type")
    private LedgerType type;

    @Column(name = "remittance_info")
    private String remittanceInfo;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "booking_date")
    private Instant bookingDate;

    @Column(name = "value_date")
    private Instant valueDate;

    @Column(name = "currency")
    private CurrencyCode currency;

    @Version
    @Column(name= "version")
    private long version;

    @ManyToOne
    @JoinColumn(name = "account_bban")
    Account account;
}
