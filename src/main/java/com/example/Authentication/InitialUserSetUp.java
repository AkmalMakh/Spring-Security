package com.example.Authentication;

import com.example.Authentication.entity.Authorities;
import com.example.Authentication.entity.Role;
import com.example.Authentication.entity.User;
import com.example.Authentication.repositories.AuthorityRepository;
import com.example.Authentication.repositories.RoleRepository;
import com.example.Authentication.repositories.UserRepository;
import com.example.Authentication.shared.utils.Roles;
import com.example.Authentication.shared.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Component
public class InitialUserSetUp {


    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder cryptPasswordEncoder;

    @Autowired
    Utils utils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public InitialUserSetUp(AuthorityRepository authorityRepository, RoleRepository roleRepository) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
    }

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event){

        Authorities readAuthority = createAuthority("READ_AUTHORITY");
        Authorities writeAuthorityAuthority = createAuthority("WRITE_AUTHORITY");
        Authorities deleteAuthority = createAuthority("DELETE_AUTHORITY");

        Collection<Authorities>userAuthorities = new ArrayList<>();
        userAuthorities.add(readAuthority);
        Role user = createRole(Roles.ROLE_USER.name(), userAuthorities);

        Collection<Authorities>adminAuthorities = new ArrayList<>();
        adminAuthorities.add(readAuthority);
        adminAuthorities.add(writeAuthorityAuthority);
        Role admin = createRole(Roles.ROLE_ADMIN.name(), adminAuthorities);


        Collection<Authorities>superAdminAuthorities = new ArrayList<>();
        superAdminAuthorities.add(readAuthority);
        superAdminAuthorities.add(writeAuthorityAuthority);
        superAdminAuthorities.add(deleteAuthority);
        Role superAdmin = createRole(Roles.ROLE_SUER_USER.name(), superAdminAuthorities);

        User adminUser = new User();
        adminUser.setLastName("Makhmudov");
        adminUser.setFirstName("Akmalkhon");
        adminUser.setEmail("Akmal@vanillapaz.com");
        adminUser.setEmailVerificationStatus(true);
        adminUser.setUserId(utils.generateRandomId(30));
        adminUser.setEncryptedPassword(cryptPasswordEncoder.encode("1234"));
        adminUser.setRoles(Arrays.asList(superAdmin));

        User fromDb = userRepository.findUserByEmail(adminUser.getEmail());
        if (fromDb == null) {
            userRepository.save(adminUser);
        }
    }

    private Authorities createAuthority(String name){
        Authorities authorities = authorityRepository.findByName(name);
        if (authorities == null){
            authorities = new Authorities();
            authorities.setName(name);
            authorityRepository.save(authorities);
        }
        return authorities;
    }

    private Role createRole(String name, Collection<Authorities>authorities){
        Role role = roleRepository.findByName(name);
        if (role == null){
            role = new Role();
            role.setName(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
