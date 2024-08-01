package com.tera.ChatAI.mapper.bo;

import com.tera.ChatAI.bo.CreditCardTransactionBO;
import com.tera.ChatAI.entity.CreditCardTransaction;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = {CustomerBOMapper.class}, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface CreditCardTransactionBOMapper {
    CreditCardTransactionBO map(CreditCardTransaction creditCardTransaction);

    List<CreditCardTransactionBO> map(List<CreditCardTransaction> creditCardTransactions);
}
