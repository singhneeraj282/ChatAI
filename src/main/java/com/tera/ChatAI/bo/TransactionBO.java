package com.tera.ChatAI.bo;

import com.neovisionaries.i18n.CurrencyCode;
import com.tera.ChatAI.dto.LedgerType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
public class TransactionBO {

    private long id;

    private BigDecimal amount;

    private LedgerType type;

    private String transactionCode;

    private String merchantCategoryCode;

    private String merchantName;

    private Instant transactionDateAndTime;

    private CurrencyCode currency;

    private long version;
}
