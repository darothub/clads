package com.decagon.clads.artisans.services.auth;

import com.decagon.clads.customer.entities.Client;
import com.decagon.clads.customer.entities.ClientDTO;

public interface CustomerProfileService {
    public ClientDTO getCustomerProfile();
    public ClientDTO updateCustomerProfile(Client client);
}
