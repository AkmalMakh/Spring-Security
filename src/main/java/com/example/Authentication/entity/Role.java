package com.example.Authentication.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;


@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User>users;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name="roles_authorities", joinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"),
                            inverseJoinColumns = @JoinColumn(name = "authorities_id", referencedColumnName = "id"))
    private Collection<Authorities>authorities;


}
