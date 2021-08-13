package com.example.demo.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    @Pattern(regexp = "^[a-zA-Z0-9]+$") //username should not contain special characters
    private String username;

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$") //min 8 character with aleast 1 letter and 1 number
    private String password;

    private Set<String> roles;
}
