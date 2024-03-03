package com.woolog.service;

import com.woolog.domain.Comment;
import com.woolog.domain.Post;
import com.woolog.exception.CommentNotFound;
import com.woolog.exception.InvalidPassword;
import com.woolog.exception.PostNotFound;
import com.woolog.repository.comment.CommentRepository;
import com.woolog.repository.post.PostRepository;
import com.woolog.request.comment.CommentCreate;
import com.woolog.request.comment.CommentDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void write(Long postId, CommentCreate request) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        // TODO 웹용 DTO와 서비스용 DTO를 구분해보자
        // TODO 서비스용 DTO에서 아래 코드를 수행하도록 할것
        Comment comment = Comment.builder()
                .author(request.getAuthor())
                .password(encryptedPassword)
                .content(request.getContent())
                .build();

        post.addComment(comment);

    }

    public void delete(Long commentId, CommentDelete request) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFound::new);

        String encryptedPassword = comment.getPassword();
        if (!passwordEncoder.matches(request.getPassword(), encryptedPassword)) {
            throw new InvalidPassword();
        }

        commentRepository.delete(comment);
    }
}
