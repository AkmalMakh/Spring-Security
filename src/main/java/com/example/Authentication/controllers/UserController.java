package com.example.Authentication.controllers;

import com.example.Authentication.model.request.UserDetailsRequestModel;

import com.example.Authentication.model.request.UserLogInRequestModel;
import com.example.Authentication.model.response.OperationName;
import com.example.Authentication.model.response.*;
import com.example.Authentication.model.response.RequestOperationStatus;
import com.example.Authentication.model.response.UserRest;

import com.example.Authentication.shared.dto.UserDto;
import com.example.Authentication.shared.utils.Roles;
import com.example.Authentication.ws.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserRest getUser(@PathVariable String id) {
        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserById(id);
        ModelMapper modelMapper = new ModelMapper();
        returnValue = modelMapper.map(userDto, UserRest.class);

        return returnValue;
    }

    @GetMapping
    public List<UserRest> getUser(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "limit", defaultValue = "25") int limit) {

        List<UserDto> users = userService.getUsers(page, limit);
        List<UserRest> returnValue = new ArrayList<>();

        users.forEach((UserDto user) -> {
            UserRest user1 = new UserRest();
            BeanUtils.copyProperties(user, user1);
            returnValue.add(user1);
        });
        return returnValue;
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue = new UserRest();

        if (userDetails.getFirstName().isEmpty()) throw new NullPointerException("the object is null");

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_USER.name())));
        UserDto createdUser = userService.createUser(userDto);
        returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }




    @PutMapping("/{id}")
    public UserRest updateUser(@RequestBody UserDetailsRequestModel model,
                               @PathVariable String id) {
        UserRest returnValue = new UserRest();
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(model, UserDto.class);

        UserDto updatedUser = userService.updateUser(id, userDto);
        returnValue = modelMapper.map(updatedUser, UserRest.class);
        return returnValue;
    }

    @PostMapping("/signIn")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Sign in with email, password", response = UserLoginResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad credentials")})
    public UserLoginResponse loginUser(@RequestBody UserLogInRequestModel userModel) {
        throw new IllegalStateException("This method should not be called. This method is implemented by Spring Security");

    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or #id == principal.userId")
//    @Secured("ROLE_SUPER_ADMIN")
    @DeleteMapping("/{id}")
    public OperationStatusModel deleteUSer(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(OperationName.DELETE.name());

        userService.deleteById(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }


    @GetMapping("/email-verification")
    public OperationStatusModel verifyEmail(@RequestParam(value = "token") String token) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(OperationName.VERIFY_EMAIL.name());

        boolean isVerified = userService.verifyEmailToken(token);

        if (isVerified) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }


        return returnValue;
    }


}
