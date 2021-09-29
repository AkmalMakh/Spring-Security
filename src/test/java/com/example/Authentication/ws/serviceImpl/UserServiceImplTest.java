package com.example.Authentication.ws.serviceImpl;

import com.example.Authentication.entity.Address;
import com.example.Authentication.entity.User;
import com.example.Authentication.repositories.UserRepository;
import com.example.Authentication.shared.dto.AddressDTO;
import com.example.Authentication.shared.dto.UserDto;
import com.example.Authentication.shared.utils.Utils;
import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    User user;
    String userId = "qwqcadsd1123";
    String password = "123456";

    @BeforeEach
    void setUp() throws Exception{
        user = new User();
        user.setId(1L);
        user.setFirstName("Akmal");
        user.setLastName("Makhmudov");
        user.setUserId(userId);
        user.setEncryptedPassword(password);
        user.setAddresses(getAddresses());
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Disabled
    void createUser() {

        when(userRepository.findUserByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("cadsd1123");
        when(utils.generateRandomId(anyInt())).thenReturn(userId);

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(password);
        when(userRepository.save(any(User.class))).thenReturn(user);



        UserDto userDto = new UserDto();
        userDto.setAddresses(getAddressesDto());
        userDto.setFirstName("Akmal");
        userDto.setLastName("Makhmudov");
        userDto.setPassword("123456");
        userDto.setEmail("akmal@gmail.com");


        UserDto storedUser = userService.createUser(userDto);

        assertNotNull(storedUser);
        assertEquals(user.getFirstName(), storedUser.getFirstName());
        assertEquals(user.getLastName(), storedUser.getLastName());
        assertNotNull(storedUser.getUserId());

        assertEquals(storedUser.getAddresses().size(), user.getAddresses().size());
        verify(utils,times(2)).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("123456");

    }

    private List<AddressDTO> getAddressesDto(){
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setType("shipping");
        addressDTO.setCity("Seoul");
        addressDTO.setCountry("South Korea");
        addressDTO.setPostalCode("ABC123");
        addressDTO.setStreetName("Gwangjin gu");

        AddressDTO billingAddress = new AddressDTO();
        billingAddress.setType("billing");
        billingAddress.setCity("Seoul");
        billingAddress.setCountry("South Korea");
        billingAddress.setPostalCode("ABC123");
        billingAddress.setStreetName("Gwangjin gu");


        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(addressDTO);
        addresses.add(billingAddress);

        return addresses;
    }

    private List<Address> getAddresses(){
        List<AddressDTO> addressDTOS = getAddressesDto();
        Type listType = new TypeToken<List<Address>>() {}.getType();

        return new ModelMapper().map(addressDTOS, listType);
    }

    @Test
    void getUser() {

        when(userRepository.findUserByEmail(anyString())).thenReturn(user);
        UserDto userDto = userService.getUser("test@test.com");

        assertNotNull(userDto);
        assertEquals("Akmal", userDto.getFirstName());
    }

    @Test
    final void testGetUser_UsernameNotFoundException(){
        when(userRepository.findUserByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, ()->{
            userService.getUser("test@test.com");
        });
    }

    @Test
    @Disabled
    void getUsers() {

    }

    @Test
    @Disabled
    void updateUser() {
        // when
        when(userRepository.findUserByEmail(anyString())).thenReturn(null);
        when(userRepository.findByUserId(anyString())).thenReturn(user);
        UserDto userDto = userService.getUserById("test@test.com");

        assertNotNull(userDto);
        assertEquals("qwqcadsd1123", userDto.getUserId());

        User updatedUser = new User();
        updatedUser = new ModelMapper().map(user, User.class);

        updatedUser.setFirstName("Aki");
        updatedUser.setLastName("Makhmudov");

//        UserDto updatedUser = userService.updateUser(userId, userDto);

        assertNotEquals(user.getFirstName(), updatedUser.getFirstName());
        assertNotEquals(user.getLastName(), updatedUser.getLastName());
    }


    @Test
    void getUserById() {
        when(userRepository.findByUserId(anyString())).thenReturn(user);

        when(userRepository.findByUserId(anyString())).thenReturn(user);

        UserDto userDto = userService.getUserById(anyString());

        assertEquals("Akmal", userDto.getFirstName());

    }
}