package com.wilyfreddie.wilypet.users;

import com.wilyfreddie.wilypet.exceptions.ApiRequestException;
import com.wilyfreddie.wilypet.security.config.EmailValidator;
import com.wilyfreddie.wilypet.security.token.ValidationToken;
import com.wilyfreddie.wilypet.security.token.ValidationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "api/v1/users")
@AllArgsConstructor
public class UserController {
    private final EmailValidator emailValidator;
    private final ValidationTokenService validationTokenService;
    private final UserService userService;


    @PostMapping("register")
    public String register(@RequestBody UserRegistrationRequest request) {
        boolean validEmail = emailValidator.test(request.getEmail());
        if (!validEmail) {
            throw new ApiRequestException("Invalid email.");
        }

        if (request.getFirstName().equals("") || request.getLastName().equals("")) {
            throw new ApiRequestException("First Name or Last Name cannot be empty.");
        }

        String token = userService.registerUser(new User(
                request.getFirstName(),
                request.getMiddleName(),
                request.getMiddleName(),
                request.getBirthday(),
                request.getAddress(),
                request.getEmail(),
                request.getPassword(),
                UserRoles.USER
        ));
        String link = "http://localhost:8080/api/v1/users/validate?token=" + token;
        return "Please confirm your email address via " + link;
    }

    @GetMapping("validate")
    public String confirm(@RequestParam("token") String token) {
        ValidationToken validationToken = validationTokenService.findByToken(token).orElseThrow(() -> new IllegalStateException("Token does not exist"));

        LocalDateTime expiresAt = validationToken.getExpiresAt();
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token is already expired.");
        }

        validationTokenService.setConfirmedAt(validationToken);
        userService.enableUser(validationToken.getUser());

        return "Email successfully validated.";
    }
}