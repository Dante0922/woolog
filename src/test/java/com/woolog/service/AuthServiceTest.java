package com.woolog.service;

import com.woolog.crypto.PasswordEncoder;
import com.woolog.domain.User;
import com.woolog.exception.AlreadyExistsEmailException;
import com.woolog.exception.InvalidSigninInformation;
import com.woolog.repository.PostRepository;
import com.woolog.repository.UserRepository;
import com.woolog.request.Login;
import com.woolog.request.PostCreate;
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
    @Test
    @DisplayName("로그인 성공")
    void test3() {

        String encrpytedPassword = passwordEncoder.encrypt("1234");
        // given
        User userA = User.builder().email("gw8888@gmail.com")
                .password(encrpytedPassword)
                .name("선가입")
                .build();
        userRepository.save(userA);

        Login login = Login.builder()
                .email("gw8888@gmail.com")
                .password("1234")
                .build();

        // when
        Long userId = authService.signin(login);
        //then
        assertNotNull(userId);
    }
    @Test
    @DisplayName("비밀번호 틀림")
    void test4() {
        // given
        Signup woolog = Signup.builder()
                .password("1234")
                .email("gw8888@gmail.com")
                .name("이건우")
                .build();
        authService.signup(woolog);

        Login login = Login.builder()
                .email("gw8888@gmail.com")
                .password("12345")
                .build();

        //expected
        assertThrows(InvalidSigninInformation.class, ()->authService.signin(login));
    }
}