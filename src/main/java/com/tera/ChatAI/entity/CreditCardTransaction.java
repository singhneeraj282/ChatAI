package com.tera.ChatAI.entity;

import com.tera.ChatAI.dto.LedgerType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "CREDIT_CARD_TRANSACTION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditCardTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "transaction_amount")
    private BigDecimal amount;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private LedgerType type;

    @Column(name = "transaction_date_time")
    private Instant transactionDateAndTime;

    @Column(name = "merchant_category_code")
    private String merchantCategoryCode;

    @Column(name = "merchant_name")
    private String merchantName;
}
