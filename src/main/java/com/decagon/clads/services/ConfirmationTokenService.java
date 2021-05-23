package com.decagon.clads.services;

import com.decagon.clads.entities.token.ConfirmationToken;
import com.decagon.clads.repositories.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private ConfirmationTokenRepository confirmationTokenRepository;

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
                confirmationToken.getArtisan(),
                confirmationToken.getCreatedAt(),
                confirmationToken.getExpiresAt(),
                confirmationToken.getToken()
        );
    }
}
