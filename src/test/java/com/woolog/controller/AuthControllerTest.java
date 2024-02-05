package com.woolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woolog.domain.Session;
import com.woolog.domain.User;
import com.woolog.repository.PostRepository;
import com.woolog.repository.SessionRepository;
import com.woolog.repository.UserRepository;
import com.woolog.request.Login;
import com.woolog.request.PostCreate;
import com.woolog.request.Signup;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test() throws Exception {

        userRepository.save(User.builder()
                .name("이건우")
                .email("gw8413@gmail.com")
                .password("1234")
                .build());

        //given
        Login login = Login.builder()
                .email("gw8413@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

//        System.out.println(json);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

    }


    @Test
    @DisplayName("로그인 성공 후 세션 응답")
    void test2() throws Exception {

        User user = userRepository.save(User.builder()
                .name("이건우")
                .email("gw8413@gmail.com")
                .password("1234")
                .build());

        //given
        Login login = Login.builder()
                .email("gw8413@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

//        System.out.println(json);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()) )
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속")
    void test4() throws Exception {

        User user = User.builder()
                .name("이건우")
                .email("gw8413@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);
        // expected
        mockMvc.perform(get("/kali")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 시 검증되지 않은 세션값으로 요청 시 오류")
    void test5() throws Exception {

        User user = User.builder()
                .name("이건우")
                .email("gw8413@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);
        // expected
        mockMvc.perform(get("/kali")
                        .header("Authorization", session.getAccessToken() + "-other")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입")
    void test6() throws Exception {

        Signup woolog = Signup.builder()
                .password("1234")
                .email("gw8888@gmail.com")
                .name("이건우")
                .build();
        // expected
        mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(woolog))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

}