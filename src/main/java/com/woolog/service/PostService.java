package com.woolog.service;

import com.woolog.domain.Post;
import com.woolog.domain.PostEditor;
import com.woolog.exception.PostNotFound;
import com.woolog.repository.PostRepository;
import com.woolog.request.PostCreate;
import com.woolog.request.PostEdit;
import com.woolog.request.PostSearch;
import com.woolog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;

    public void write(PostCreate postCreate){
        //postCreate -> Entity 변환이 필요하다.
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        postRepository.save(post);

    }

    public PostResponse get(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public List<PostResponse> getList(PostSearch postSearch) {

//        Pageable pageable = PageRequest.of(
//                page, 5, Sort.by(Sort.Direction.DESC,"id"));

        return postRepository.getList(postSearch).stream()
                .map(post -> new PostResponse(post))
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

//        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();
//
//        if(postEdit.getTitle() != null){
//            editorBuilder.title(postEdit.getTitle());
//        }
//        if(postEdit.getContent() != null){
//            editorBuilder.content(postEdit.getContent());
//        }
//        post.edit(editorBuilder.build());

        post.edit(postEdit.getTitle(), postEdit.getContent());
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}
