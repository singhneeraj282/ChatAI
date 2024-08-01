package com.tera.ChatAI.repository;

import com.tera.ChatAI.entity.DebitCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitCardTransactionRepository extends JpaRepository<DebitCardTransaction, Long> {
}
