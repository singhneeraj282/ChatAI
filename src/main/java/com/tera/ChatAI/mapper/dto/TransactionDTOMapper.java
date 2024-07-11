package com.tera.ChatAI.mapper.dto;

import com.tera.ChatAI.bo.TransactionBO;
import com.tera.ChatAI.dto.TransactionDTO;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(componentModel = "spring", uses = {AccountDTOMapper.class}, unmappedSourcePolicy = ERROR, unmappedTargetPolicy = ERROR)
public interface TransactionDTOMapper {
    TransactionDTO map(TransactionBO transactionBO);

    List<TransactionDTO> map(List<TransactionBO> transactionBOCollection);

}
