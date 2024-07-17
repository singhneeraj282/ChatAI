package com.tera.ChatAI.mapper.dto;

import com.tera.ChatAI.bo.CustomerBO;
import com.tera.ChatAI.dto.CustomerDTO;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.ERROR;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = {AccountDTOMapper.class}, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface CustomerDTOMapper {
    CustomerDTO map(CustomerBO customerBO);

    List<CustomerDTO> map(List<CustomerBO> customerBOs);
}
