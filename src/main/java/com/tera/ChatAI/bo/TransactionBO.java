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

    private String counterPartyName;

    private String counterPartyAccount;

    private LedgerType type;

    private String remittanceInfo;

    private BigDecimal amount;

    private Instant bookingDate;

    private Instant valueDate;

    private CurrencyCode currency;

    private long version;

    AccountBO account;
}
