package com.tera.ChatAI.repository;

import com.tera.ChatAI.entity.PersonalisedContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalisedContentRepository extends JpaRepository<PersonalisedContent, Long> {
    PersonalisedContent findBySegmentAndPersonaAndTypeAndMessageType(String segment, String persona,  String type, String messageType);
}
