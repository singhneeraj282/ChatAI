package com.tera.ChatAI.mapper.bo;

import com.tera.ChatAI.bo.AccountBO;
import com.tera.ChatAI.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(componentModel = "spring", uses = {TransactionBOMapper.class, CustomerBOMapper.class}, unmappedSourcePolicy = ERROR, unmappedTargetPolicy = ERROR)
public interface AccountBOMapper {
    AccountBO map(Account account);

    List<AccountBO> map(List<Account> accounts);
}
