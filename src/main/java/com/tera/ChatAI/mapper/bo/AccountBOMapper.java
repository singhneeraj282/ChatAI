package com.tera.ChatAI.mapper.bo;

import com.tera.ChatAI.bo.AccountBO;
import com.tera.ChatAI.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = {TransactionBOMapper.class, CustomerBOMapper.class, CreditCardBOMapper.class}, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface AccountBOMapper {
    AccountBO map(Account account);

    List<AccountBO> map(List<Account> accounts);
}
