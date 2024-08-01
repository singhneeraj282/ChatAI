package com.tera.ChatAI.mapper.bo;

import com.tera.ChatAI.bo.CreditCardBO;
import com.tera.ChatAI.entity.CreditCard;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = {CreditCardTransactionBOMapper.class}, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface CreditCardBOMapper {
    CreditCardBO map(CreditCard creditCard);

    List<CreditCardBO> map(List<CreditCard> creditCards);
}
