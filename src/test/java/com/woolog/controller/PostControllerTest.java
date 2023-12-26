package com.woolog.controller;

import com.woolog.domain.Post;
import com.woolog.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청 시 Hello World를 출력한다")
    void test() throws Exception {
        //글 제목
        //글 내용
        // 사용자
            // id
            // name
            // level
            // title=xx&content=xx&user.id=xx&user.name=xx&user.level=xx
            // ㄴ 한계가 있다.

        /***
         * JSON의 장점
         * {
         *     "title": "글 제목입니다.",
         *     "content": "글 내용입니다.",
         *     "user": {
         *          "id": 1,
         *          "name": "홍길동",
         *          "level": 1
         *       }
         * }
         * */

        // expected
        mockMvc.perform(post("/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("title", "글 제목입니다.")
//                        .param("content", "글 내용입니다.")
                        .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\": \"글 제목입니다.\", \"content\": \"글 내용입니다\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());

    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수다")
    void test2() throws Exception{
        mockMvc.perform(post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
//                                .content("{\"title\": \"\", \"content\": \"글 내용입니다\"}")
                                .content("{\"title\": null, \"content\": \"글 내용입니다\"}")
                )
                .andExpect(status().isBadRequest())
                /*jsonPath는 따로 공부해보자*/
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
        //

    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception{
        // when
        mockMvc.perform(post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\": \"제목입니디\", \"content\": \"내용입니다\"}")
                )
                .andExpect(status().isOk())
                .andDo(print());
        //then
        assertThat(postRepository.count()).isEqualTo(1L);
        Post post = postRepository.findAll().get(0);

        assertThat(post.getTitle()).isEqualTo("제목입니디");
        assertThat(post.getContent()).isEqualTo("내용입니다");


    }


}