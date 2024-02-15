package com.woolog.service;

import com.woolog.domain.User;
import com.woolog.exception.AlreadyExistsEmailException;
import com.woolog.repository.UserRepository;
import com.woolog.request.Signup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given
        Signup woolog = Signup.builder()
                .password("1234")
                .email("gw8888@gmail.com")
                .name("이건우")
                .build();
        // when
        authService.signup(woolog);
        // then
        Assertions.assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();

        assertEquals("gw8888@gmail.com", user.getEmail());
        assertNotEquals("1234", user.getPassword());
        assertEquals("이건우", user.getName());
    }

    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void test2() {

        User userA = User.builder().email("gw8888@gmail.com")
                .password("11112")
                .name("선가입")
                .build();

        userRepository.save(userA);


        // given
        Signup woolog = Signup.builder()
                .password("1234")
                .email("gw8888@gmail.com")
                .name("이건우")
                .build();
        // when
        assertThrows(AlreadyExistsEmailException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                authService.signup(woolog);
            }
        });
    }
}