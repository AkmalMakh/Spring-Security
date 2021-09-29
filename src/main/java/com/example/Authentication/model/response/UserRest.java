package com.example.Authentication.model.response;

import com.example.Authentication.entity.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class UserRest {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AddressRest>addresses;


}
