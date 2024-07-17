package com.tera.ChatAI.mapper.bo;

import com.tera.ChatAI.bo.DebitCardTransactionBO;
import com.tera.ChatAI.entity.DebitCardTransaction;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = {CustomerBOMapper.class}, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface DebitCardTransactionBOMapper {
    DebitCardTransactionBO map(DebitCardTransaction debitCardTransaction);

    List<DebitCardTransactionBO> map(List<DebitCardTransaction> debitCardTransactions);
}
