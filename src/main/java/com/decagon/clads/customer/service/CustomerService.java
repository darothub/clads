package com.decagon.clads.customer.service;

import com.decagon.clads.artisans.entities.Artisan;
import com.decagon.clads.artisans.repositories.ArtisanRepository;
import com.decagon.clads.confirmation.entities.ConfirmationToken;
import com.decagon.clads.confirmation.services.ConfirmationTokenService;
import com.decagon.clads.customer.entities.Client;
import com.decagon.clads.customer.repositories.ClientRepository;
import com.decagon.clads.exceptions.CustomException;
import com.decagon.clads.jwt.JWTUtility;
import com.decagon.clads.model.dto.CladUser;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.utils.AUTHPROVIDER;
import com.decagon.clads.utils.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomerService implements UserDetailsService {
    private final ClientRepository clientRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtility jwtUtility;
    private final ObjectMapper objectMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Client> clientRepositoryByEmail = clientRepository.findByEmail(email);
        try{
            Client c = clientRepositoryByEmail.get();
            GrantedAuthority authority = new SimpleGrantedAuthority(Role.CLIENT.name());
            if(!c.isEnabled()){
                throw new IllegalStateException("User has not been verified");
            }
            else if (c.getAuthprovider() == AUTHPROVIDER.GOOGLE){
                return c;
            }
            return new User(c.getUsername(), c.getPassword(), Collections.singletonList(authority));
        }catch (Exception exception){
            throw new UsernameNotFoundException("User not authorized.");
        }
    }
    public String signUpCustomer(Client client){
        Optional<Client> clientExisting = clientRepository.findByEmail(client.getEmail());
        if (clientExisting.isEmpty()) {
            String encodedPassword = bCryptPasswordEncoder.encode(client.getPassword());
            client.setPassword(encodedPassword);
            client.setCreatedAt(LocalDateTime.now());
            Client newClient = clientRepository.save(client);
            String token = jwtUtility.generateToken(objectMapper.convertValue(newClient, CladUser.class));
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    newClient.getEmail()
            );
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            return token;
        }
        else if (!clientExisting.get().isEnabled()){
            String token = jwtUtility.generateToken(objectMapper.convertValue(clientExisting.get(), CladUser.class));
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    clientExisting.get().getEmail()
            );
            log.info("Client ConfirmationToken {}", confirmationToken);
            confirmationTokenService.updateConfirmationToken(confirmationToken);
            return token;
        }
        else {
            ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.toString(), Optional.of(String.format("%s already taken", client.getEmail())));
            throw new CustomException(error);
        }
    }
    public int enableCustomer(String email) {
        return clientRepository.enableCustomer(email);
    }
}
