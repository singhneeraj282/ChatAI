package com.tera.ChatAI.mapper.dto;

import com.tera.ChatAI.bo.CreditCardBO;
import com.tera.ChatAI.dto.CreditCardDTO;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = {CreditCardTransactionDTOMapper.class}, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface CreditCardDTOMapper {
    CreditCardDTO map(CreditCardBO creditCardBO);

    List<CreditCardDTO> map(List<CreditCardBO> creditCardBOS);
}
