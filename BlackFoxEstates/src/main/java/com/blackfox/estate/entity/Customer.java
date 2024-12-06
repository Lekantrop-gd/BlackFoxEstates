package com.blackfox.estate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="customer")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class Customer {
    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="email", unique=true)
    @Email(message = "Email should be valid")
    private String email;

    @Column(name="phone_number", unique=true)
    @Pattern(regexp = "^\\+\\d{11,13}$", message = "Phone number must match \"+12345678901\" format")
    private String phoneNumber;
}