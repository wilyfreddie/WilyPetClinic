package com.wilyfreddie.wilypet.security.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ValidationTokenService {
    private final ValidationTokenRepository validationTokenRepository;

    public void saveValidationToken(ValidationToken validationToken) {
        validationTokenRepository.save(validationToken);
    }

    public void setConfirmedAt(ValidationToken validationToken) {
        validationToken.setConfirmedAt(LocalDateTime.now());
        validationTokenRepository.save(validationToken);
    }

    public Optional<ValidationToken> findByToken(String token) {
        return validationTokenRepository.findByToken(token);
    }
}