package com.woolog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String mainPage() {
        return "메인 페이지입니다.☺️";
    }

    @GetMapping("/user")
    public String user() {
        return "사용자 페이지입니다.😍";
    }
    @GetMapping("/admin")
    public String admin() {
        return "관리자 페이지입니다.😘";
    }

}
