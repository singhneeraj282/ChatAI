package com.tera.ChatAI.bo;

import com.tera.ChatAI.dto.LedgerType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
public class CreditCardTransactionBO {

    private long id;

    private BigDecimal amount;

    private LedgerType type;

    private Instant transactionDateAndTime;

    private String merchantCategoryCode;

    private String merchantName;

}
