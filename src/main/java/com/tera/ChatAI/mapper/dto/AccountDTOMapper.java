package com.tera.ChatAI.mapper.dto;

import com.tera.ChatAI.bo.AccountBO;
import com.tera.ChatAI.dto.AccountDTO;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.ERROR;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = {CustomerDTOMapper.class, TransactionDTOMapper.class}, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface AccountDTOMapper {
    AccountDTO map(AccountBO accountBO);

    List<AccountDTO> map(List<AccountBO> accountBOs);
}
