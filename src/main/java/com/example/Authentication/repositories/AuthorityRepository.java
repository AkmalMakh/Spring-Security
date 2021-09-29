package com.example.Authentication.repositories;

import com.example.Authentication.entity.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authorities, Long> {
    Authorities findByName(String name);
}

