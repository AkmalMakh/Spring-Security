package com.example.Authentication.shared.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class UserDto implements Serializable {
    private static final long serialVersionUID = 486590303910150223L;
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus;
    private List<AddressDTO> addresses;
    private Collection<String>roles;
}
