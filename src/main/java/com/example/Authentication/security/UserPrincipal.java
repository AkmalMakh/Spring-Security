package com.example.Authentication.security;

import com.example.Authentication.entity.Authorities;
import com.example.Authentication.entity.Role;
import com.example.Authentication.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;


public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = -7530187709860249942L;

    private User user;
    private String userId;

    public UserPrincipal(User user) {
        this.user = user;
        this.userId = user.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorityList =new HashSet<>();
        Collection<Authorities>authorities = new HashSet<>();
        // Get user Roles
        Collection<Role>roles = user.getRoles();

        if (roles == null) return authorityList;

        roles.forEach(role -> {
            authorityList.add(new SimpleGrantedAuthority(role.getName()));
            authorities.addAll(role.getAuthorities());
        });

        authorities.forEach((authorities1 -> {
            authorityList.add(new SimpleGrantedAuthority(authorities1.getName()));
        }));


        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.user.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.getEmailVerificationStatus();
    }

    public String getUserId() {
        return userId;
    }
}
