package com.example.Authentication.model.response;

import lombok.Data;


@Data
public class UserLoginResponse {
    private String name;
    private String userId;
    private String access_token;
    private String refresh_token;
}
