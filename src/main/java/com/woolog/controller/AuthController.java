package com.woolog.controller;

import com.woolog.request.Login;
import com.woolog.response.SessionResponse;
import com.woolog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        log.info(">>>>login={}", login);

        String accessToken = authService.signin(login);
        return new SessionResponse(accessToken);
    }

}
