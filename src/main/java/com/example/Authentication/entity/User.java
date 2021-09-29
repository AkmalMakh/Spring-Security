package com.example.Authentication.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name="user")
public class User implements Serializable {
    private static final long serialVersionUID = 4865901203910150223L;

    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;


    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String encryptedPassword;
    private String emailVerificationToken;
    @Column(nullable = false)
    private Boolean emailVerificationStatus=false;

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.ALL}, mappedBy = "user")
    @JsonManagedReference
    private List<Address> addresses;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name="user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
                           inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"))
    private Collection<Role>roles;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void AddAddress(Address address){
        if (address == null)
            address = new Address();
        address.setUser(this);
    }
}
