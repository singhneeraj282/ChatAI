package com.tera.ChatAI.repository;

import com.tera.ChatAI.entity.PersonalisedContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalisedContentRepository extends JpaRepository<PersonalisedContent, Long> {
    PersonalisedContent findBySegmentAndTypeAndMessageType(String segment, String type, String messageType);
}
