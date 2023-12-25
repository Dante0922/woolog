package com.woolog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("타이틀을 입력해주세요."))
                .andDo(print());
        //

    }


}