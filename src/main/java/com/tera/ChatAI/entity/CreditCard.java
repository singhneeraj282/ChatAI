package com.tera.ChatAI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "CREDIT_CARD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "credit_card_number")
    private String cardNumber;

    @Column(name = "type")
    private String type;

    @Column(name = "fee")
    private BigDecimal fee;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Version
    @Column(name= "version")
    private long version;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_card_id", referencedColumnName = "id")
    private Set<CreditCardTransaction> cardTransactions;
}
