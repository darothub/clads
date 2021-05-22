package com.decagon.clads.services;

import com.decagon.clads.entities.Artisan;
import com.decagon.clads.exceptions.CustomException;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.repositories.ArtisanRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;


@AllArgsConstructor
@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final ArtisanRepository artisanRepository;
    private final ErrorResponse error;
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
}
