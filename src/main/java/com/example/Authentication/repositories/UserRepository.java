package com.example.Authentication.repositories;

import com.example.Authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByEmail(String email);

    User findByUserId(String id);

    User findUserByEmailVerificationToken(String token);
}
