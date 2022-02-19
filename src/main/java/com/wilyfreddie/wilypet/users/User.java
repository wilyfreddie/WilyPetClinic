package com.wilyfreddie.wilypet.users;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "SITE_USER")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(name = "owner_sequence", sequenceName = "owner_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "owner_sequence")
    private long id;
    @Column(nullable = false)
    private String firstName;
    private String middleName;
    @Column(nullable = false)
    private String lastName;
    private LocalDate birthday;
    private String address;
    private String email;
    private String password;
    private String specialty;

    @Enumerated(EnumType.STRING)
    private UserRoles userRoles;

    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean locked;
    private boolean enabled;

    public User(String firstName, String middleName, String lastName, LocalDate birthday, String address, String email, String password, UserRoles userRoles) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.address = address;
        this.email = email;
        this.password = password;
        this.userRoles = userRoles;
    }

    public User(String firstName, String middleName, String lastName, LocalDate birthday, String address, String email, String password, UserRoles userRoles, String specialty) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.address = address;
        this.email = email;
        this.password = password;
        this.userRoles = userRoles;
        this.specialty = specialty;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userRoles.name());
        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}