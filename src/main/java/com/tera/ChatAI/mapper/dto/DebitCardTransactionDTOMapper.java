package com.tera.ChatAI.mapper.dto;

import com.tera.ChatAI.bo.DebitCardTransactionBO;
import com.tera.ChatAI.dto.DebitCardTransactionDTO;
import com.tera.ChatAI.mapper.bo.CustomerBOMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = {CustomerBOMapper.class}, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface DebitCardTransactionDTOMapper {
    DebitCardTransactionDTO map(DebitCardTransactionBO debitCardTransactionBO);

    List<DebitCardTransactionDTO> map(List<DebitCardTransactionBO> debitCardTransactionBOS);
}
