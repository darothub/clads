package com.decagon.clads.services;

import com.decagon.clads.entities.artisan.Address;
import com.decagon.clads.entities.artisan.Artisan;
//import com.decagon.clads.entities.artisan.Union;
import com.decagon.clads.entities.artisan.Association;
import com.decagon.clads.entities.token.ConfirmationToken;
import com.decagon.clads.exceptions.CustomException;
import com.decagon.clads.jwt.JWTUtility;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.repositories.ArtisanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@AllArgsConstructor
@Service
@Slf4j
public class ArtisanService implements UserDetailsService {

    private final ArtisanRepository artisanRepository;
    private final ConfirmationTokenService confirmationTokenService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtility jwtUtility;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Artisan> artisan = artisanRepository.findByEmail(email);
        try{
            Artisan a = artisan.get();
            GrantedAuthority authority = new SimpleGrantedAuthority(a.getRole());
            return new User(a.getUsername(), a.getPassword(), Collections.singletonList(authority));
        }catch (Exception exception){
            throw new UsernameNotFoundException("User not authorized.");
        }
    }

    public String signUpArtisan(Artisan artisan){
        Optional<Artisan> artisanExists = artisanRepository.findByEmail(artisan.getEmail());
        if (artisanExists.isEmpty()) {
            String encodedPassword = bCryptPasswordEncoder.encode(artisan.getPassword());
            artisan.setPassword(encodedPassword);
            Artisan newArtisan = artisanRepository.save(artisan);
            String token = jwtUtility.generateToken(newArtisan);
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    newArtisan
            );

            log.info("ConfirmationToken {}", confirmationToken);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            return token;
        }
        else if (!artisanExists.get().isEnabled()){
            String token = jwtUtility.generateToken(artisanExists.get());
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    artisanExists.get()
            );
            log.info("ConfirmationToken {}", confirmationToken);
            confirmationTokenService.updateConfirmationToken(confirmationToken);
            return token;
        }
        else {
            ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.toString(), String.format("%s already taken", artisan.getEmail()));
            throw new CustomException(error);
        }
    }
    public int enableArtisan(String email) {
        return artisanRepository.enableArtisan(email);
    }
}
