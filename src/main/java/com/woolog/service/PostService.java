package com.woolog.service;

import com.woolog.domain.Post;
import com.woolog.repository.PostRepository;
import com.woolog.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;

    public void write(PostCreate postCreate){

        //postCreate -> Entity 변환이 필요하다.

        Post post = new Post(postCreate.getTitle(), postCreate.getContent());

        postRepository.save(post);

    }
}
