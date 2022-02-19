package com.wilyfreddie.wilypet.users;

import com.wilyfreddie.wilypet.exceptions.ApiRequestException;
import com.wilyfreddie.wilypet.security.token.ValidationToken;
import com.wilyfreddie.wilypet.security.token.ValidationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final static String MSG_USER_NOT_FOUND = "User with email %s not found.";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidationTokenService validationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiRequestException("User not found."));

        return user;
    }

    public String registerUser(User user) {
        boolean userExists = userRepository
                .findByEmail(user.getEmail())
                .isPresent();

        if (userExists) {
            throw new IllegalStateException("User already exists.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ValidationToken validationToken = new ValidationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        validationTokenService.saveValidationToken(validationToken);

//        TODO: SEND EMAIL
        return token;
    }

    public void enableUser(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

}