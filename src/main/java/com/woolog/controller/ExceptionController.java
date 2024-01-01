package com.woolog.controller;

import com.woolog.exception.InvalidRequest;
import com.woolog.exception.WoologException;
import com.woolog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return errorResponse;
    }

    @ResponseBody
    //@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WoologException.class)
    public ResponseEntity<ErrorResponse> woologException(WoologException e) {

        int statusCode = e.getStatusCode();

        ErrorResponse response = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        // 응답 json validation -> title: 제목에 바보를 포함할 수 없습니다.
//        if (e instanceof InvalidRequest invalidRequest) {
//            String fieldName = invalidRequest.getFieldName();
//            String message = invalidRequest.getMessage();
//            response.addValidation(fieldName, message);
//        }

        return ResponseEntity.status(statusCode)
                .body(response);

    }
}
