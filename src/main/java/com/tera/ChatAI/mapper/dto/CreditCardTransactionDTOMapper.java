package com.tera.ChatAI.mapper.dto;

import com.tera.ChatAI.bo.CreditCardTransactionBO;
import com.tera.ChatAI.dto.CreditCardTransactionDTO;
import com.tera.ChatAI.mapper.bo.CustomerBOMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = {CustomerBOMapper.class}, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface CreditCardTransactionDTOMapper {
    CreditCardTransactionDTO map(CreditCardTransactionBO creditCardTransactionBO);

    List<CreditCardTransactionDTO> map(List<CreditCardTransactionBO> creditCardTransactionBOS);
}
