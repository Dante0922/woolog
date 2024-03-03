package com.woolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woolog.config.WoologMockUser;
import com.woolog.domain.Comment;
import com.woolog.domain.Post;
import com.woolog.domain.User;
import com.woolog.repository.UserRepository;
import com.woolog.repository.comment.CommentRepository;
import com.woolog.repository.post.PostRepository;
import com.woolog.request.comment.CommentCreate;
import com.woolog.request.comment.CommentDelete;
import com.woolog.request.post.PostCreate;
import com.woolog.request.post.PostEdit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @WoologMockUser
    @DisplayName("댓글 작성")
    void test1() throws Exception {
        // given
        Post post = Post.builder()
                .title("123456789012345")
                .content("화이팅!!")
                .build();
        postRepository.save(post);


        CommentCreate comment = CommentCreate.builder().author("건우")
                .password("333333")
                .content("댓글댓글")
                .build();


        String json = objectMapper.writeValueAsString(comment);

        // when
        mockMvc.perform(post("/posts/{postId}/comments", post.getId())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(json))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(1L, commentRepository.count());
        Comment comment1 = commentRepository.findAll().get(0);
        assertEquals("건우",comment1.getAuthor() );
        assertNotEquals("333333",comment1.getPassword());
        assertTrue(passwordEncoder.matches("333333", comment1.getPassword()));
        assertEquals("댓글댓글",comment1.getContent());
    }

    @Test
    @DisplayName("댓글 삭제")
    @WoologMockUser
    void test2() throws Exception{
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("화이팅!!")
                .build();
        postRepository.save(post);

        String encryptedPassword = passwordEncoder.encode("333333");

        Comment comment = Comment.builder().author("건우")
                .password(encryptedPassword)
                .content("댓글댓글")
                .build();
        comment.setPost(post);
        commentRepository.save(comment);

        CommentDelete request = new CommentDelete("333333");
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/comments/{commentId}/delete", comment.getId())                                  .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());



    }

}

