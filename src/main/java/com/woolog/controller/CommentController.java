package com.woolog.controller;


import com.woolog.request.comment.CommentCreate;
import com.woolog.request.comment.CommentDelete;
import com.woolog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public void write(@PathVariable Long postId, @RequestBody @Valid CommentCreate request){

        // 웹용 DTO와 서비스용 DTO를 구분해보자
        commentService.write(postId, request);
    }
    @PostMapping("/comments/{commentId}/delete")
    public void delete(@PathVariable Long commentId, @RequestBody @Valid CommentDelete request){
        commentService.delete(commentId, request);
    }
}
