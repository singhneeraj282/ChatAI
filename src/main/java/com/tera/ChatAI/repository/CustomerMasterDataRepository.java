package com.tera.ChatAI.repository;

import com.tera.ChatAI.entity.CustomerMasterData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMasterDataRepository extends JpaRepository<CustomerMasterData, Long> {
}
