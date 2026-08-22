package com.careerflow.exception;

public class UserAlreadyExistingException extends RuntimeException{

    public UserAlreadyExistingException(String message){
        super(message);
    }
}
