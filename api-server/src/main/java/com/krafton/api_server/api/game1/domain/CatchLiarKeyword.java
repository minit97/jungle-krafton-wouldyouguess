package com.krafton.api_server.api.game1.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CatchLiarKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catch_liar_keyword_id")
    private Long id;

    private String liarKeyword;
    private String keyword;

    @Builder
    public CatchLiarKeyword(String liarKeyword, String keyword) {
        this.liarKeyword = liarKeyword;
        this.keyword = keyword;
    }
}
