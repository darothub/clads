package com.decagon.clads.confirmation.services;

import com.decagon.clads.confirmation.entities.ConfirmationToken;
import com.decagon.clads.confirmation.repositories.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken){
        confirmationTokenRepository.save(confirmationToken);
    }
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
    public void updateConfirmationToken(ConfirmationToken confirmationToken){
        confirmationTokenRepository.updateConfirmationToken(
                confirmationToken.getEmail(),
                confirmationToken.getCreatedAt(),
                confirmationToken.getExpiresAt(),
                confirmationToken.getToken()
        );
    }
}
