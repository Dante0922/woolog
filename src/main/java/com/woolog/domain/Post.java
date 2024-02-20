package com.woolog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob // java에서는 String으로 가지고 있지만 DB에는 Long으로 저장되도록
    private String content;

    @ManyToOne
    @JoinColumn
    private User user;

    @Builder
    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void edit(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Long getUserId() {
        return this.id;
    }
}
