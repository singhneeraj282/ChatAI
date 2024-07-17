package com.tera.ChatAI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "CUSTOMER_MASTER_DATA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerMasterData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "avg_monthly_balance")
    private BigDecimal avgMonthlyBalance;

    @Column(name = "total_inflows")
    private int totalInflows;

    @Column(name = "total_outflows")
    private int totalOutflows;

    @Column(name = "age")
    private int age;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_master_data_id", referencedColumnName = "id")
    private Set<CustomerMerchantCategory> customerMerchantCategories;

}