package com.tera.ChatAI.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
public class CreditCardDTO {

    private long id;

    private String cardNumber;

    private String type;

    private BigDecimal fee;

    private Set<CreditCardTransactionDTO> cardTransactions;

}
