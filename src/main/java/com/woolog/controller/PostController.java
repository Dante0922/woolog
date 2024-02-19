package com.woolog.controller;

import com.woolog.request.PostCreate;
import com.woolog.request.PostEdit;
import com.woolog.request.PostSearch;
import com.woolog.response.PostResponse;
import com.woolog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        /* 인증을 받는 방법
        * 1. GET Parameter
        * 2. POST(body) value <- 별로임
        * 3. Header */

            request.validate();
            postService.write(request);


    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId){
        postService.delete(postId);

    }
}
