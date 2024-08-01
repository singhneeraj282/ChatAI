package com.tera.ChatAI.mapper.bo;

import com.tera.ChatAI.bo.CustomerBO;
import com.tera.ChatAI.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;


@Mapper(componentModel = "spring", uses = {AccountBOMapper.class}, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface CustomerBOMapper {
    CustomerBO map(Customer customer);

    List<CustomerBO> map(List<Customer> customers);
}
