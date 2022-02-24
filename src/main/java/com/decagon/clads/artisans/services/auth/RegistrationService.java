package com.decagon.clads.artisans.services.auth;

import com.decagon.clads.customer.entities.ClientDTO;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.dto.CladUser;

public interface RegistrationService {
    ArtisanDTO registerArtisan(CladUser cladUser);
    ClientDTO registerCustomer(CladUser cladUser);
    public boolean confirmToken(String token);
    void sendEmail(String token, String cladUser, String cladUser1);
}
