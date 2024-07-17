package com.tera.ChatAI.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
public class CreditCardBO {

    private long id;

    private String cardNumber;

    private String type;

    private BigDecimal fee;

    private Set<CreditCardTransactionBO> cardTransactions;

}
