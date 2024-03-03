package com.woolog.repository.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woolog.domain.Post;
import com.woolog.request.post.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.woolog.domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Post> getList(PostSearch postSearch) {



        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.id.desc())
                .fetch();
    }

}
