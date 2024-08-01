package com.tera.ChatAI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "PERSONALIED_CONTENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalisedContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "segment")
    private String segment;

    @Column(name = "type")
    private String type;

    @Column(name = "message_type")
    private String messageType;

    @Column(name = "content")
    private String content;

    /*@Column(name = "heading")
    private String heading;

    @Column(name = "subtext")
    private String subtext;

    @Column(name = "cta")
    private String cta;

    @Column(name = "offer_reasoning")
    private String offerReasoning;

    @Column(name = "reasoning")
    private String reasoning;*/

    @Version
    @Column(name= "version")
    private long version;

}
