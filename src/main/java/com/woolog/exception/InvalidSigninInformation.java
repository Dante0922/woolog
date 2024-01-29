package com.woolog.exception;

public class InvalidSigninInformation extends WoologException{


    private static final String MESSAGE = "아이디/비밀번호가 옳지 않습니다.";

    public InvalidSigninInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
