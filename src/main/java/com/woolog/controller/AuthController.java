package com.woolog.controller;

import com.woolog.config.AppConfig;
import com.woolog.request.Login;
import com.woolog.response.SessionResponse;
import com.woolog.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;
    private final AppConfig appConfig;


    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        log.info(">>>>login={}", login);
        Long userId = authService.signin(login);

//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        byte[] encodedKey = key.getEncoded();
//        String strKey = Base64.getEncoder().encodeToString(encodedKey);



        // key 디코딩
        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(key)
                .setIssuedAt(new Date())
                .compact();

        return new SessionResponse(jws);

//        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
//                .domain("localhost") // todo 서버 환경에 따른 분리 필요
//                .path("/")
//                .httpOnly(true)
//                .secure(false)
//                .maxAge(Duration.ofDays(30))
//                .sameSite("Strict")
//                .build();
//
//        log.info(">>>>> cookie={}", cookie.toString());
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
        //return new SessionResponse(accessToken);
    }

}
