package com.tera.ChatAI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "DEBIT_CARD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebitCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "debit_card_number")
    private String cardNumber;

    @Column(name = "type")
    private String type;

    @Column(name = "fee")
    private BigDecimal fee;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "debit_card_id", referencedColumnName = "id")
    private Set<DebitCardTransaction> cardTransactions;
}
