package com.example.Authentication.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name="addresses")
public class Address implements Serializable {
    private static final long serialVersionUID = 7809200551672852690L;

    @javax.persistence.Id
    @Id
    @GeneratedValue
    private Long id;

    @Column(length=30, nullable=false)
    private String addressId;

    @Column(length=15, nullable=false)
    private String city;

    @Column(length=15, nullable=false)
    private String country;

    @Column(length=100, nullable=false)
    private String streetName;

    @Column(length=7, nullable=false)
    private String postalCode;

    @Column(length=10, nullable=false)
    private String type;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JsonBackReference
    private User user;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
