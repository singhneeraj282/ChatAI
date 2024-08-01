package com.tera.ChatAI.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SEGMENT_DEFINITION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SegmentDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "segment")
    private String segment;

    @Column(name = "definition")
    private String definition;

    @Version
    @Column(name= "version")
    private long version;

}
