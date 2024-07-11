package com.tera.ChatAI.mapper.dto;

import com.tera.ChatAI.bo.AccountBO;
import com.tera.ChatAI.dto.AccountDTO;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(componentModel = "spring", uses = {CustomerDTOMapper.class, TransactionDTOMapper.class}, unmappedSourcePolicy = ERROR, unmappedTargetPolicy = ERROR)
public interface AccountDTOMapper {
    AccountDTO map(AccountBO accountBO);

    List<AccountDTO> map(List<AccountBO> accountBOs);
}
