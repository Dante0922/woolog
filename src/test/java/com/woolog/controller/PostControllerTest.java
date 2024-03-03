package com.woolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woolog.config.WoologMockUser;
import com.woolog.domain.Post;
import com.woolog.domain.User;
import com.woolog.repository.post.PostRepository;
import com.woolog.repository.UserRepository;
import com.woolog.request.post.PostCreate;
import com.woolog.request.post.PostEdit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

//    @Test
//    @DisplayName("글 작성 요청 시 Hello world를 출력한다")
//    void test() throws Exception {
//
//        //given
//        PostCreate request = PostCreate.builder()
//                .title("제목입니다")
//                .content("내용입니다")
//                .build();
//
//        String json = objectMapper.writeValueAsString(request);
//
////        System.out.println(json);
//
//        // expected
//        mockMvc.perform(post("/posts?authorization=hello")
//                        .contentType(APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andExpect(content().string(""))
//                .andDo(print());
//    }

    @Test
    @DisplayName("글 작성 요청시 title값은 필수다")
    void test2() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다")
                .build();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                /*jsonPath는 따로 공부해보자*/
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
        //

    }

    @Test
    @WoologMockUser()
    @DisplayName("글 작성")
    void test3() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);


        // when
        mockMvc.perform(post("/posts")
                        .header("authorization", "woolog")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
        //then
        assertThat(postRepository.count()).isEqualTo(1L);
        Post post = postRepository.findAll().get(0);

        assertThat(post.getTitle()).isEqualTo("제목입니다");
        assertThat(post.getContent()).isEqualTo("내용입니다");
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given

        Post post = Post.builder()
                .title("123456789012345")
                .content("화이팅!!")
                .build();
        postRepository.save(post);


        //expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("화이팅!!"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 다건 조회")
    void test5() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("우로그 제목 " + i)
                            .content("우로그 내용 " + i)
                            .build();
                }).toList();
        postRepository.saveAll(requestPosts);


        //expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("우로그 제목 30"))
                .andExpect(jsonPath("$[0].content").value("우로그 내용 30"))
                .andExpect(jsonPath("$[4].title").value("우로그 제목 26"))
                .andExpect(jsonPath("$[4].content").value("우로그 내용 26"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 조회")
    void test6() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("우로그 제목 " + i)
                            .content("우로그 내용 " + i)
                            .build();
                }).toList();
        postRepository.saveAll(requestPosts);


        //expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("우로그 제목 30"))
                .andExpect(jsonPath("$[0].content").value("우로그 내용 30"))
                .andExpect(jsonPath("$[4].title").value("우로그 제목 26"))
                .andExpect(jsonPath("$[4].content").value("우로그 내용 26"))
                .andDo(print());
    }
    @Test
    @WoologMockUser()
    @DisplayName("게시글 수정")
    void test7() throws Exception {
        //given

        User user = userRepository.findAll().get(0);
        Post post = Post.builder()
                .title("이건우")
                .content("성공하자!")
                .user(user)
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("이크림")
                .content("hello")
                .build();


        //expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
//    @Test
//    @WoologMockUser()
//    @DisplayName("게시글 삭제")
//    void test8() throws Exception {
//        //given
//        User user = userRepository.findAll().get(0);
//
//        Post post = Post.builder()
//                .title("이건우")
//                .content("성공하자!")
//                .user(user)
//                .build();
//        postRepository.save(post);
//
//
//
//        //expected
//        mockMvc.perform(delete("/posts/{postId}", post.getId())
//                        .contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andDo(print());
//    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception{
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WoologMockUser()
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception{

        //given
        PostEdit postEdit = PostEdit.builder()
                .title("이크림")
                .content("hello")
                .build();


        //expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

//    @Test
//    @DisplayName("게시글 제목에 '바보'는 포함될 수 없다")
//    void test11() throws Exception {
//        //given
//        PostCreate request = PostCreate.builder()
//                .title("나는 바보가 아니다")
//                .content("내용입니다")
//                .build();
//
//        String json = objectMapper.writeValueAsString(request);
//
//        // when
//        mockMvc.perform(post("/posts")
//                        .contentType(APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isBadRequest())
//                .andDo(print());
//    }
}

//