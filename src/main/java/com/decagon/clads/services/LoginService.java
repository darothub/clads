package com.decagon.clads.services;

import com.decagon.clads.email.EmailSender;
import com.decagon.clads.entities.artisan.Artisan;
import com.decagon.clads.entities.artisan.AuthRole;
import com.decagon.clads.entities.token.ConfirmationToken;
import com.decagon.clads.jwt.JWTUtility;
import com.decagon.clads.model.request.LoginRequest;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.repositories.ArtisanRepository;
import com.decagon.clads.utils.AUTHPROVIDER;
import com.decagon.clads.utils.ConstantUtils;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class LoginService {
    private final ArtisanRepository artisanRepository;
    private final EmailSender emailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtility jwtUtility;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConstantUtils constantUtils;
    private ErrorResponse errorResponse;
    public String loginService(LoginRequest loginRequest){
        Artisan artisan = artisanRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new IllegalStateException("Invalid username/password"));
//        log.info("Artisan {}", artisan);
        if(!artisan.isEnabled()){
            String token = jwtUtility.generateToken(artisan);
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    artisan
            );
            confirmationTokenService.updateConfirmationToken(confirmationToken);
            String link = String.format(constantUtils.host+"confirm?token=%s", token);
            emailSender.send(artisan.getEmail(), emailSender.buildEmail(artisan.getFirstName(), link));
            throw new IllegalStateException("Kindly verify your email address");
        }
        boolean verifyPassword = bCryptPasswordEncoder.matches(loginRequest.getPassword(), artisan.getPassword());
        if(verifyPassword){
            return jwtUtility.generateToken(artisan);
        }
        throw new IllegalStateException("Invalid username/password");
    }
    @Transactional
    public String loginWithGoogleService(String auth, AuthRole role){
        String token;
        try{
            if (auth.isBlank()){
                throw new AuthenticationException("Invalid auth credential");
            }
            final HttpTransport transport = new NetHttpTransport();
            final GsonFactory jsonFactory = new GsonFactory();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setIssuers(Arrays.asList("https://accounts.google.com", "accounts.google.com"))
                    .setAudience(Collections.singletonList(ConstantUtils.CLIENT_ID))
                    .build();
//            log.info("verify things filter");
            token = auth.substring(7);
//            log.info("Token {}", token);

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
                    return jwtUtility.generateToken(isOldUser.get());
                }
                else if (isOldUser.isEmpty() && role.getRole() != null) {
                    String pictureUrl = (String) payload.get("picture");
                    String familyName = (String) payload.get("family_name");
                    String givenName = (String) payload.get("given_name");
                    Artisan artisan = new Artisan(
                            givenName,
                            familyName,
                            "",
                            role.getRole(),
                            pictureUrl,
                            email,
                            AUTHPROVIDER.GOOGLE
                    );
//                    log.info("Artisan {}", artisan);
                    Artisan newArtisan = artisanRepository.save(artisan);
                    newArtisan.setEnabled(true);
                    return jwtUtility.generateToken(newArtisan);
                }
                else if(isOldUser.isPresent() && role.getRole() != null ){
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
        catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
}
