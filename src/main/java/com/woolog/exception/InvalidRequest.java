package com.woolog.exception;

import lombok.Getter;

/***
 * status -> 400
 */
@Getter
public class InvalidRequest extends WoologException {

    private static final String MESSAGE = "잘못된 요청입니다.";

//    public String message;
//    public String fieldName;

    public InvalidRequest(){
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message){
        super(MESSAGE);
//        this.fieldName = fieldName;
//        this.message = message;
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode(){
        return 400;
    }
}
