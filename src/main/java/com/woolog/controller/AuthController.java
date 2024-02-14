package com.woolog.controller;

import com.woolog.config.AppConfig;
import com.woolog.request.Signup;
import com.woolog.service.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup){
        authService.signup(signup);
    }

    @GetMapping("/auth/login")
    public String login() {
        return "로그인 페이지입니다.";
    }
}
