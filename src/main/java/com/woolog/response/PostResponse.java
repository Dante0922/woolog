package com.woolog.response;

import com.woolog.domain.Post;
import lombok.Builder;
import lombok.Getter;

/***
 * 서비스 정책에 따라서 필요한 데이터만 반환하도록 한다.
 */
@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    public PostResponse(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
    }

}
