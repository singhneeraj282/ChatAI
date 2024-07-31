package com.tera.ChatAI.repository;

import com.tera.ChatAI.entity.PersonalisedContent;
import com.tera.ChatAI.entity.SegmentDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SegmentDefinitionRepository extends JpaRepository<SegmentDefinition, Long> {
    SegmentDefinition findSegmentDefinitionBySegment(String segment);
}
