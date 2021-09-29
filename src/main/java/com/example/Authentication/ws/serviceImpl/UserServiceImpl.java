package com.example.Authentication.ws.serviceImpl;

import com.example.Authentication.entity.Address;
import com.example.Authentication.entity.Role;
import com.example.Authentication.entity.User;
import com.example.Authentication.exceptions.UserServiceException;
import com.example.Authentication.model.response.ErrorMessages;
import com.example.Authentication.repositories.RoleRepository;
import com.example.Authentication.repositories.UserRepository;
import com.example.Authentication.security.UserPrincipal;
import com.example.Authentication.shared.dto.AddressDTO;
import com.example.Authentication.shared.dto.UserDto;
import com.example.Authentication.shared.utils.*;
import com.example.Authentication.ws.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final Utils utils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Utils utils, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto createUser(UserDto user) {


        if (userRepository.findUserByEmail(user.getEmail()) != null)
            throw new UserServiceException("Record already exists");

        if (user.getAddresses() != null) {
            for (int i = 0; i < user.getAddresses().size(); i++) {
                AddressDTO address = user.getAddresses().get(i);
                address.setAddressId(utils.generateAddressId(30));
            }
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        User userEntity = modelMapper.map(user, User.class);

        if (user.getAddresses() != null) {
        userEntity.getAddresses().forEach((Address address)->{
            userEntity.AddAddress(address);
        });
        }
        String publicUserId = utils.generateRandomId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
        userEntity.setEmailVerificationStatus(false);
        userEntity.setId(null);

        //SET ROLES
        Collection<Role>roles = new HashSet<>();
        for(String role: user.getRoles()){
            Role role1 = roleRepository.findByName(role);
            if (role1 != null){
                roles.add(role1);
            }
        }
        userEntity.setRoles(roles);
        User storedUserDetails = userRepository.save(userEntity);

//        //Send an email message to user to verify their email
//        new AmazonSES().verifyEmail(returnValue);
        storedUserDetails.setRoles(null);

        return modelMapper.map(storedUserDetails, UserDto.class);
}

    @Override
    public UserDto getUser(String email) {
        User user = userRepository.findUserByEmail(email);
        if(user == null) throw new UsernameNotFoundException(email);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(user,returnValue);
        return returnValue;
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto>returnValue = new ArrayList<>();
        Page<User> userPage = userRepository.findAll(PageRequest.of(page,limit));
        List<User> users = userPage.getContent();
        users.forEach((User user)->{
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user,userDto);
            returnValue.add(userDto);
        });
        return returnValue;
    }

    @Override
    public UserDto getUserById(String id) {
        User user = userRepository.findByUserId(id);
        if(user == null) throw new UsernameNotFoundException(id);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto) {
        User user = userRepository.findByUserId(id);
        if(user == null) throw new UsernameNotFoundException(id);


        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        User updatedUser = userRepository.save(user);
        UserDto returnValue = new ModelMapper().map(updatedUser, UserDto.class);

        return returnValue;
    }

    @Override
    public boolean verifyEmailToken(String token) {
        boolean returnValue = false;

        User user = userRepository.findUserByEmailVerificationToken(token);

        if (user != null){
            boolean hasTokenExpired = Utils.hasTokenExpired(token);
            if (!hasTokenExpired){
                user.setEmailVerificationToken(null);
                user.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(user);
                returnValue = true;
            }
        }
        return returnValue;
    }

    @Override
    public void deleteById(String id) {
        User userEntity = userRepository.findByUserId(id);

        if (userEntity == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);

        if(user == null) throw new UsernameNotFoundException(email);

        return new UserPrincipal(user);

}
}
