package com.woolog.controller;

import com.woolog.request.PostCreate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PostController {
    // SSR -> jsp, thymeleaf, mustache, freemarker
    //     ->   html rendering
    // SPA ->
    //      vue -> vue+SSR = nuxt
    //      react -> react+SSR = next
    //      javascript + <-> API (JSON)

    @GetMapping("/posts")
    public String get() {
        return "Hello World!";
    }
    // HTTP Method
    // GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, TRACE, CONNECT

    // 글 등록
    @PostMapping("/posts")
    //public String post(@RequestParam String title, @RequestParam String content) {
    //public String post(@RequestParam Map<String, String> params) {
//    public String post(@ModelAttribute PostCreate params) {
    public Map<String,String> post(@RequestBody @Valid PostCreate params, BindingResult result) {
        log.info("params={}", params.toString());

        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String fieldName = firstFieldError.getField();
            String errorMessage = firstFieldError.getDefaultMessage();

            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);

            return error;

        }
        return Map.of();

        // 데이터를 검증하는 이유
        // 1. client 개발자가 깜박할 수 있따.
        // 2. client bug로 값이 누락될 수 있다.
        // 3. 외부에서 값을 임의로 조작해서 보낼 수 있다.
        // 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
        // 5. 서버 개발자의 편안함을 위해서


        /*
         * 1. 파라미터가 많을 경우 if문 노가다를 해야함
         * 2. 개발팁 -> 무언가 3번 이상 반복 작업을할 땐 잘못하고 있는 건 아닌지 의심해야함
         * 3. 누락될 염려가 있다.
         * 4. 생각보다 검증해야할 게 많다.(꼼꼼하지 않을 수 있다.)
         * 5. 뭔가 개발자스럽지 않다. -> 간지가 안 난다.
         *
         * -> DTO에 validation - NotBlank 등을 활용하자.
         * */

//        String title = params.getTitle();
//        String content = params.getContent();
//
//        if(title == null || title.equals("")) {
//            throw new Exception("title이 누락되었습니다.");
//
//        }
//        if(content == null || content.equals("")) {
//            //error
//        }
//        return "Hello World!";
    }


}
