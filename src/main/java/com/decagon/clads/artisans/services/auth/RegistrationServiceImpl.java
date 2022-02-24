package com.decagon.clads.artisans.services.auth;

import com.decagon.clads.confirmation.services.ConfirmationTokenService;
import com.decagon.clads.customer.entities.Client;
import com.decagon.clads.customer.entities.ClientDTO;
import com.decagon.clads.customer.service.CustomerService;
import com.decagon.clads.email.EmailSender;
import com.decagon.clads.artisans.entities.Artisan;
import com.decagon.clads.confirmation.entities.ConfirmationToken;
import com.decagon.clads.exceptions.CustomException;
import com.decagon.clads.jwt.JWTUtility;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.dto.CladUser;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.utils.ConstantUtils;
import com.decagon.clads.utils.Role;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
@Slf4j
@CacheConfig(cacheNames = {"registration"})
public class RegistrationServiceImpl implements RegistrationService {

    private final ArtisanService artisanService;
    private final CustomerService customerService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final ModelMapper modelMapper;
    private final ErrorResponse errorResponse;
    private final ConstantUtils constantUtils;
    private final JWTUtility jwtUtility;

    @Cacheable
    @Transactional
    @Override
    public boolean confirmToken(String token) {

        ConfirmationToken confirmationToken = getConfirmationToken(token);

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("link has expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        String role = jwtUtility.getRoleFromToken(token);
        if (role.equals(Role.TAILOR.name())){
            artisanService.enableArtisan(
                    confirmationToken.getEmail());
            return true;
        }
        else if(role.equals(Role.CLIENT.name())) {
            customerService.enableCustomer(confirmationToken.getEmail());
            return true;
        }
        return false;
    }

    public ConfirmationToken getConfirmationToken(String token){
        return confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found/has expired"));
    }

    @Override
    public ArtisanDTO registerArtisan(CladUser cladUser) {
        try{
            Artisan artisan = modelMapper.map(cladUser, Artisan.class);
            String token = artisanService.signUpArtisan(artisan);
            sendEmail(token, cladUser.getEmail(), cladUser.getFirstName());
            return modelMapper.map(cladUser, ArtisanDTO.class);
        }
        catch (Exception e){
            errorResponse.setMessage(e.getMessage());
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public ClientDTO registerCustomer(CladUser cladUser) {
        try{
            String token = customerService.signUpCustomer(modelMapper.map(cladUser, Client.class));
            sendEmail(token, cladUser.getEmail(), cladUser.getFirstName());
            return modelMapper.map(cladUser, ClientDTO.class);
        }
        catch (Exception e){
            errorResponse.setMessage(e.getMessage());
            throw new CustomException(errorResponse);
        }
    }
    @Override
    public void sendEmail(String token, String cladUser, String cladUser1) {
        String link = String.format(constantUtils.host+"confirm?token=%s", token);
        emailSender.send(cladUser, emailSender.buildEmail(cladUser1, link));
    }
}
