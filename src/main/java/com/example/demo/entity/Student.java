package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    @NotBlank(message = "firsName should be not empty")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "lastName should be not empty")
    private String lastName;

    @Column(name = "email")
    @NotBlank(message = "email should be not empty and in valid format")
    @Email
    private String email;
}