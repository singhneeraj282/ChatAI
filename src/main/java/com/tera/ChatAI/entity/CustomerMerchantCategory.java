package com.tera.ChatAI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "CUSTOMER_MERCHANT_CATEGORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerMerchantCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "merchant_type")
    private String merchantType;

    @Column(name = "merchant_amount")
    private BigDecimal merchantAmount;

    @Version
    @Column(name= "version")
    private long version;
}
