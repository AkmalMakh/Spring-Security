package com.example.Authentication.model.request;

import lombok.Data;

@Data
public class UserLogInRequestModel {
    private String email;
    private String password;
}
