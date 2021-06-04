package com.decagon.clads.services;

import com.decagon.clads.email.EmailSender;
import com.decagon.clads.entities.artisan.Artisan;
import com.decagon.clads.entities.token.ConfirmationToken;
import com.decagon.clads.jwt.JWTUtility;
import com.decagon.clads.model.request.LoginRequest;
import com.decagon.clads.repositories.ArtisanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class LoginService {
    private final ArtisanRepository artisanRepository;
    private final EmailSender emailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtility jwtUtility;
    private final ConfirmationTokenService confirmationTokenService;
    public String loginService(LoginRequest loginRequest){
        Artisan artisan = artisanRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new IllegalStateException("Invalid username/password"));
        log.info("Artisan {}", artisan);
        if(!artisan.isEnabled()){
            String token = jwtUtility.generateToken(artisan);
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    artisan
            );
            confirmationTokenService.updateConfirmationToken(confirmationToken);
            String link = String.format("http://localhost:8080/api/v1/confirm?token=%s", token);
            emailSender.send(artisan.getEmail(), emailSender.buildEmail(artisan.getFirstName(), link));
            throw new IllegalStateException("Kindly verify your email address");
        }
        boolean verifyPassword = bCryptPasswordEncoder.matches(loginRequest.getPassword(), artisan.getPassword());
        if(verifyPassword){
            return jwtUtility.generateToken(artisan);
        }
        throw new IllegalStateException("Invalid username/password");
    }

    public String loginWithGoogleService(String auth){
//        jwtUtility.validateToken()
//        Artisan artisan = artisanRepository.findByEmail(loginRequest.getEmail())
//                .orElseThrow(()-> new IllegalStateException("Invalid username/password"));
//        log.info("Artisan {}", artisan);
//        if(!artisan.isEnabled()){
//            String token = jwtUtility.generateToken(artisan);
//            ConfirmationToken confirmationToken = new ConfirmationToken(
//                    token,
//                    LocalDateTime.now(),
//                    LocalDateTime.now().plusMinutes(15),
//                    artisan
//            );
//            confirmationTokenService.updateConfirmationToken(confirmationToken);
//            String link = String.format("http://localhost:8080/api/v1/confirm?token=%s", token);
//            emailSender.send(artisan.getEmail(), emailSender.buildEmail(artisan.getFirstName(), link));
//            throw new IllegalStateException("Kindly verify your email address");
//        }
//        boolean verifyPassword = bCryptPasswordEncoder.matches(loginRequest.getPassword(), artisan.getPassword());
//        if(verifyPassword){
//            return jwtUtility.generateToken(artisan);
//        }
        throw new IllegalStateException("Invalid username/password");
    }
}
