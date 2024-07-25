package com.tera.ChatAI.entity;

import com.neovisionaries.i18n.CurrencyCode;
import com.tera.ChatAI.dto.LedgerType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "CUSTOMER_TRANSACTION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "type")
    private LedgerType type;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "merchant_category_code")
    private String merchantCategoryCode;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "transaction_date_time")
    private Instant transactionDateAndTime;

    @Column(name = "currency")
    private CurrencyCode currency;

    @Version
    @Column(name = "version")
    private long version;
}
