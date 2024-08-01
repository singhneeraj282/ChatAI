package com.tera.ChatAI.bo;

import com.neovisionaries.i18n.CurrencyCode;
import com.tera.ChatAI.dto.AccountStatus;
import com.tera.ChatAI.dto.AccountType;
import com.tera.ChatAI.dto.LedgerType;
import com.tera.ChatAI.entity.CustomerMerchantCategory;
import com.tera.ChatAI.entity.DebitCardTransaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Builder
public class AccountBO {

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

    private List<TransactionBO> transactions;

    private List<CreditCardBO> creditCards;

    private List<DebitCardBO> debitCards;

    public long totalAccountInflows() {
        var creditCardsTransactionsInflows = creditCards.stream()
                .map(CreditCardBO::getCardTransactions)
                .flatMap(Collection::stream)
                .filter(x -> x.getType().equals(LedgerType.Credit))
                .count();
        var debitCardsTransactionsInflows = debitCards.stream()
                .map(DebitCardBO::getCardTransactions)
                .flatMap(Collection::stream)
                .filter(x -> x.getType().equals(LedgerType.Credit))
                .count();
        var transactionInflows = transactions.stream()
                .filter(x -> x.getType().equals(LedgerType.Credit))
                .count();
        return creditCardsTransactionsInflows + debitCardsTransactionsInflows + transactionInflows;

    }

    public long totalAccountOutflows() {
        var creditCardsTransactionsOutflows = creditCards.stream()
                .map(CreditCardBO::getCardTransactions)
                .flatMap(Collection::stream)
                .filter(x -> x.getType().equals(LedgerType.Debit))
                .count();
        var debitCardsTransactionsOutflows = debitCards.stream()
                .map(DebitCardBO::getCardTransactions)
                .flatMap(Collection::stream)
                .filter(x -> x.getType().equals(LedgerType.Debit))
                .count();
        var transactionOutflows = transactions.stream()
                .filter(x -> x.getType().equals(LedgerType.Debit))
                .count();
        return creditCardsTransactionsOutflows + debitCardsTransactionsOutflows + transactionOutflows;

    }

    public Map<String, BigDecimal> accountMerchantCategories() {
        var creditCardMerchantCategory = creditCards.stream()
                .map(CreditCardBO::getCardTransactions)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(CreditCardTransactionBO::getMerchantCategoryCode, Collectors.reducing(BigDecimal.ZERO,
                        CreditCardTransactionBO::getAmount,
                        BigDecimal::add)));

        var debitCardMerchantCategory = debitCards.stream()
                .map(DebitCardBO::getCardTransactions)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(DebitCardTransactionBO::getMerchantCategoryCode, Collectors.reducing(BigDecimal.ZERO,
                        DebitCardTransactionBO::getAmount,
                        BigDecimal::add)));

        var transactionCardMerchantCategory = transactions.stream()
                .collect(Collectors.groupingBy(TransactionBO::getMerchantCategoryCode, Collectors.reducing(BigDecimal.ZERO,
                        TransactionBO::getAmount,
                        BigDecimal::add)));

        return Stream.of(creditCardMerchantCategory, debitCardMerchantCategory, transactionCardMerchantCategory)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, BigDecimal::add));
    }
}
