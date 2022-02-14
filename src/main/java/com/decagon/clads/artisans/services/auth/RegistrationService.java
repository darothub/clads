package com.decagon.clads.artisans.services.auth;

import com.decagon.clads.confirmation.services.ConfirmationTokenService;
import com.decagon.clads.email.EmailSender;
import com.decagon.clads.artisans.entities.Artisan;
import com.decagon.clads.confirmation.entities.ConfirmationToken;
import com.decagon.clads.exceptions.CustomException;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
@Slf4j
@CacheConfig(cacheNames = {"registration"})
public class RegistrationService {

    private final ArtisanService artisanService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final ModelMapper modelMapper;
    private final ErrorResponse errorResponse;
    private final ConstantUtils constantUtils;

    @Cacheable
    public CompletableFuture<Object> register(Artisan artisan) {
        ArtisanDTO artisanDTO = modelMapper.map(artisan, ArtisanDTO.class);
        return CompletableFuture.supplyAsync(() -> artisanService.signUpArtisan(artisan)).handle((res, e)->{

            if(e != null){
                errorResponse.setMessage(e.getMessage());
                throw new CustomException(errorResponse);
            }
            String link = String.format(constantUtils.host+"confirm?token=%s", res);
            emailSender.send(artisanDTO.getEmail(), emailSender.buildEmail(artisanDTO.getFirstName(), link));
            return res;
        });
    }


    @Cacheable
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
