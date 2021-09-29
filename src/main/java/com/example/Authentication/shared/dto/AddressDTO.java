package com.example.Authentication.shared.dto;

import lombok.Data;


@Data
public class AddressDTO {
    private Long id;
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
    private UserDto userDto;
}
