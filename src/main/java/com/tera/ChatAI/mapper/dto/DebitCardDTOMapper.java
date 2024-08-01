package com.tera.ChatAI.mapper.dto;

import com.tera.ChatAI.bo.DebitCardBO;
import com.tera.ChatAI.dto.DebitCardDTO;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = {DebitCardTransactionDTOMapper.class}, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface DebitCardDTOMapper {
    DebitCardDTO map(DebitCardBO debitCardBO);

    List<DebitCardDTO> map(List<DebitCardBO> debitCardBOS);
}
