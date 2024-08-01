package com.tera.ChatAI.mapper.bo;

import com.tera.ChatAI.bo.TransactionBO;
import com.tera.ChatAI.entity.Transaction;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = {AccountBOMapper.class}, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface TransactionBOMapper {
    TransactionBO map(Transaction transaction);

    List<TransactionBO> map(List<Transaction> transactions);

}
