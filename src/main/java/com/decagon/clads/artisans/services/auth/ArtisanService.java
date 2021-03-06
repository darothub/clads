package com.decagon.clads.artisans.services.auth;

import com.decagon.clads.artisans.entities.Artisan;
//import com.decagon.clads.entities.artisan.Union;
import com.decagon.clads.confirmation.services.ConfirmationTokenService;
import com.decagon.clads.confirmation.entities.ConfirmationToken;
import com.decagon.clads.exceptions.CustomException;
import com.decagon.clads.jwt.JWTUtility;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.dto.CladUser;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.artisans.repositories.ArtisanRepository;
import com.decagon.clads.utils.AUTHPROVIDER;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
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

@RequiredArgsConstructor
@Service
@Slf4j
@CacheConfig(cacheNames = {"artisan"})
public class ArtisanService implements UserDetailsService {

    private final ArtisanRepository artisanRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtility jwtUtility;
    private final ObjectMapper objectMapper;

    @Cacheable
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Artisan> artisan = artisanRepository.findByEmail(email);
        try{
            Artisan a = artisan.get();
            GrantedAuthority authority = new SimpleGrantedAuthority(a.getRole().name());
            if(!a.isEnabled()){
                throw new IllegalStateException("User has not been verified");
            }
            else if (a.getAuthprovider() == AUTHPROVIDER.GOOGLE){
                return a;
            }
            return new User(a.getUsername(), a.getPassword(), Collections.singletonList(authority));
        }catch (Exception exception){
            throw new UsernameNotFoundException("User not authorized.");
        }
    }
    @Cacheable
    public String signUpArtisan(Artisan artisan){
        Optional<Artisan> artisanExists = artisanRepository.findByEmail(artisan.getEmail());
        if (artisanExists.isEmpty()) {
            String encodedPassword = bCryptPasswordEncoder.encode(artisan.getPassword());
            artisan.setPassword(encodedPassword);
            Artisan newArtisan = artisanRepository.save(artisan);

            String token = jwtUtility.generateToken(objectMapper.convertValue(newArtisan, CladUser.class));
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    newArtisan.getEmail()
            );
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            return token;
        }
        else if (!artisanExists.get().isEnabled()){
            String token = jwtUtility.generateToken(objectMapper.convertValue(artisanExists.get(), CladUser.class));
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    artisanExists.get().getEmail()
            );
            log.info("ConfirmationToken {}", confirmationToken);
            confirmationTokenService.updateConfirmationToken(confirmationToken);
            return token;
        }
        else {
            ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.toString(), Optional.of(String.format("%s already taken", artisan.getEmail())));
            throw new CustomException(error);
        }
    }
    public int enableArtisan(String email) {
        return artisanRepository.enableArtisan(email);
    }
}
