package com.decagon.clads.artisans.services.auth;

import com.decagon.clads.confirmation.services.ConfirmationTokenService;
import com.decagon.clads.customer.entities.Client;
import com.decagon.clads.customer.repositories.ClientRepository;
import com.decagon.clads.email.EmailSender;
import com.decagon.clads.artisans.entities.Artisan;
import com.decagon.clads.confirmation.entities.ConfirmationToken;
import com.decagon.clads.jwt.JWTUtility;
import com.decagon.clads.model.dto.CladUser;
import com.decagon.clads.model.request.LoginRequest;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.artisans.repositories.ArtisanRepository;
import com.decagon.clads.utils.AUTHPROVIDER;
import com.decagon.clads.utils.ConstantUtils;
import com.decagon.clads.utils.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class LoginService {
    private final ArtisanRepository artisanRepository;
    private final ClientRepository clientRepository;
    private final EmailSender emailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtility jwtUtility;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConstantUtils constantUtils;
    private final ObjectMapper objectMapper;

    private ErrorResponse errorResponse;
    public String loginArtisanService(LoginRequest loginRequest){
        Artisan artisan = artisanRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new IllegalStateException("Invalid username/password"));
        return confirmUserAndGenerateToken(objectMapper.convertValue(artisan, CladUser.class), loginRequest);
    }
    public String loginCustomerService(LoginRequest loginRequest){
        Client client = clientRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new IllegalStateException("Invalid username/password"));
        return confirmUserAndGenerateToken(objectMapper.convertValue(client, CladUser.class), loginRequest);
    }
    private String confirmUserAndGenerateToken(CladUser cladUser, LoginRequest loginRequest) {
        isUserEnabled(cladUser);
        boolean verifyPassword = bCryptPasswordEncoder.matches(loginRequest.getPassword(), cladUser.getPassword());
        if(verifyPassword){
            return jwtUtility.generateToken(cladUser);
        }
        throw new IllegalStateException("Invalid username/password");
    }

    private void isUserEnabled(CladUser cladUser) {
        if(!cladUser.isEnabled()){
            String token = jwtUtility.generateToken(cladUser);
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    cladUser.getEmail()
            );
            confirmationTokenService.updateConfirmationToken(confirmationToken);
            String link = String.format(constantUtils.host+"confirm?token=%s", token);
            emailSender.send(cladUser.getEmail(), emailSender.buildEmail(cladUser.getFirstName(), link));
            throw new IllegalStateException("Kindly verify your email address");
        }
    }


    @Transactional
    public String loginWithGoogleService(String auth, Role role){
        String token;
        try{
            if (auth.isBlank()){
                throw new AuthenticationException("Invalid auth credential");
            }
            final HttpTransport transport = new NetHttpTransport();
            final GsonFactory jsonFactory = new GsonFactory();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setIssuers(Arrays.asList("https://accounts.google.com", "accounts.google.com"))
                    .setAudience(Collections.singletonList(ConstantUtils.ARTISAN_CLIENT_ID))
                    .build();
            GoogleIdTokenVerifier customerVerifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setIssuers(Arrays.asList("https://accounts.google.com", "accounts.google.com"))
                    .setAudience(Collections.singletonList(ConstantUtils.CUSTOMER_CLIENT_ID))
                    .build();
//            log.info("verify things filter");
            token = auth.substring(7);
            log.info("Token {} role {}", token, role);

            if (role.name().equals(Role.TAILOR.name())){
                return verifyArtisan(role, token, verifier);
            }
            return verifyCustomer(role, token, customerVerifier);
        }
        catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    private String verifyArtisan(Role role, String token, GoogleIdTokenVerifier verifier) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = verifier.verify(token);
        if (null != idToken) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            log.info("IdToken {}", idToken);
            String email = idToken.getPayload().getEmail();
            Optional<Artisan> isOldUser = artisanRepository.findByEmail(email);
            if (isOldUser.isEmpty() && role == null ){
                throw new IllegalStateException("Not a registered artisan");
            }
            else if(isOldUser.isPresent() && role == null){
                return jwtUtility.generateToken((objectMapper.convertValue(isOldUser.get(), CladUser.class)));
            }
            else if (isOldUser.isEmpty() && role.equals(Role.valueOf(Role.TAILOR.name()))) {
                String pictureUrl = (String) payload.get("picture");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");
                Artisan artisan = new Artisan(
                        givenName,
                        familyName,
                        familyName,
                        email,
                        Role.CLIENT,
                        pictureUrl,
                        AUTHPROVIDER.GOOGLE
                );
                artisan.setEnabled(true);
                Artisan newArtisan = artisanRepository.save(artisan);
                return jwtUtility.generateToken(objectMapper.convertValue(newArtisan, CladUser.class));
            }
            else if(isOldUser.isPresent() && !role.name().isEmpty() ){
                throw new IllegalStateException("User already registered, please log in");
            }
            else{
                throw new IllegalStateException("Unknown user");
            }
        }
        else{
            throw new IllegalStateException("IdToken is null");
        }
    }
    private String verifyCustomer(Role role, String token, GoogleIdTokenVerifier verifier) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = verifier.verify(token);
        if (null != idToken) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = idToken.getPayload().getEmail();
            Optional<Client> isOldUser = clientRepository.findByEmail(email);
            if (isOldUser.isEmpty() && role == null ){
                throw new IllegalStateException("Not a registered user");
            }
            else if(isOldUser.isPresent() && role == null){
                return jwtUtility.generateToken(objectMapper.convertValue(isOldUser.get(), CladUser.class));
            }
            else if (isOldUser.isEmpty() && role.equals(Role.valueOf(Role.CLIENT.name()))) {
                String pictureUrl = (String) payload.get("picture");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");

                Client client = new Client(
                        givenName,
                        familyName,
                        email,
                        Role.CLIENT,
                        pictureUrl,
                        AUTHPROVIDER.GOOGLE,
                        LocalDateTime.now()
                );
                client.setEnabled(true);
                Client newClient = clientRepository.save(client);
                return jwtUtility.generateToken(objectMapper.convertValue(newClient, CladUser.class));
            }
            else if(isOldUser.isPresent() && !role.name().isEmpty() ){
                throw new IllegalStateException("User already registered, please log in");
            }
            else{
                throw new IllegalStateException("Unknown user");
            }
        }
        else{
            throw new IllegalStateException("IdToken is null");
        }
    }
}
