package com.tera.ChatAI.mapper.bo;

import com.tera.ChatAI.bo.DebitCardBO;
import com.tera.ChatAI.entity.DebitCard;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = {DebitCardTransactionBOMapper.class}, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface DebitCardBOMapper {
    DebitCardBO map(DebitCard debitCard);

    List<DebitCardBO> map(List<DebitCard> debitCards);
}
