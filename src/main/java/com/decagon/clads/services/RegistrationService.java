package com.decagon.clads.services;

import com.decagon.clads.email.EmailSender;
import com.decagon.clads.entities.artisan.Artisan;
import com.decagon.clads.entities.token.ConfirmationToken;
import com.decagon.clads.exceptions.CustomException;
import com.decagon.clads.filter.JwtFilter;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.response.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
@Slf4j
public class RegistrationService {

    private final ArtisanService artisanService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final ModelMapper modelMapper;
    private final ErrorResponse errorResponse;

    public CompletableFuture<Object> register(Artisan artisan) {
        ArtisanDTO artisanDTO = modelMapper.map(artisan, ArtisanDTO.class);
        log.info("Thread {}", Thread.currentThread());
        return CompletableFuture.supplyAsync(() -> artisanService.signUpArtisan(artisan)).handle((res, e)->{

            if(e != null){
                errorResponse.setError(e.getMessage());
                throw new CustomException(errorResponse);
            }
            String link = String.format("http://localhost:8080/api/v1/confirm?token=%s", res);
            log.info("Thread2 {}", Thread.currentThread());
            emailSender.send(artisanDTO.getEmail(), emailSender.buildEmail(artisanDTO.getFirstName(), link));
            return res;
        });
    }

    @Transactional
    public String confirmToken(String token) {

        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found/has expired"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("link has expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        artisanService.enableArtisan(
                confirmationToken.getArtisan().getEmail());
        return "confirmed";
    }

}
