package com.tera.ChatAI.dto;

import com.neovisionaries.i18n.CurrencyCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
public class AccountDTO {

    private long accountId;

    private AccountType accountType;

    private String ibanNumber;//International bank account

    private String ibanName;

    private String pan;

    private CurrencyCode currency;

    private AccountStatus status;

    private BigDecimal balance;

    private BigDecimal fee;

    private BigDecimal minBalance;

    private BigDecimal avg_balance;

    private Instant lastChange;

    private long version;

    private List<TransactionDTO> transactions;

    private List<CreditCardDTO> creditCards;

    private List<DebitCardDTO> debitCards;
}
