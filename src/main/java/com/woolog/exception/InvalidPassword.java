package com.woolog.exception;

public class InvalidPassword extends WoologException{


    private static final String MESSAGE = "비밀번호가 옳지 않습니다.";

    public InvalidPassword() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
