package com.example.Authentication.exceptions;

public class UserServiceException extends RuntimeException{
    private static final long serialVersionUID = 1345771109171435607L;

    public UserServiceException(String message){
        super(message);
    }

}
