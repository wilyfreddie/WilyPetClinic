package com.wilyfreddie.wilypet.users;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRegistrationRequest {
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final LocalDate birthday;
    private final String address;
    private final String email;
    private final String password;
    private final String specialty;
}