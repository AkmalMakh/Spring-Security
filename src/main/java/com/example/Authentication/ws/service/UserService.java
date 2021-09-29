package com.example.Authentication.ws.service;

import com.example.Authentication.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService  extends UserDetailsService {
    UserDto createUser(UserDto user);
    UserDto getUser(String email);

    List<UserDto> getUsers(int page, int limit);

    UserDto getUserById(String id);

    UserDto updateUser(String id, UserDto userDto);

    boolean verifyEmailToken(String token);

    void deleteById(String id);
}
