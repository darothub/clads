package com.decagon.clads.services.client;

import com.decagon.clads.entities.artisan.Address;
import com.decagon.clads.entities.client.Client;
import com.decagon.clads.entities.client.Measurement;
import com.decagon.clads.filter.JwtFilter;
import com.decagon.clads.repositories.client.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
@Service
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    public Client addClient(Client client){
        Collection<Client> isOldClientWithPhoneNumberAndEmail = clientRepository.findClientByPhoneNumberAndEmail(client.getPhoneNumber(), client.getEmail(), JwtFilter.userId);
        log.info("Existed number {}", isOldClientWithPhoneNumberAndEmail);
        if (!isOldClientWithPhoneNumberAndEmail.isEmpty()){
            throw new IllegalStateException("Client already exist");
        }
        client.setArtisanId(JwtFilter.userId);
        return clientRepository.save(client);
    }

    public Collection<Client> getClients() {
        return clientRepository.getClientsByArtisanId(JwtFilter.userId);
    }

    public String addDeliveryAddress(Address address, String id) {

        try{
            Long clientId = (long) Integer.parseInt(id);
            Client client = clientRepository.getById(clientId);
            log.info("Delivery address {}", client.getDeliveryAddresses());
            client.getDeliveryAddresses().add(address);
            clientRepository.save(client);
            return "Address added successfully";
        }
        catch (Exception e){
           throw new IllegalStateException(e.getMessage());
        }

    }

    public String addMeasurements(Set<Measurement> measurement, String id) {
        try{
            Long clientId = (long) Integer.parseInt(id);
            Client client = clientRepository.getById(clientId);
            log.info("Delivery address {}", client.getDeliveryAddresses());
            client.getMeasurements().addAll(measurement);
            clientRepository.save(client);
            return "Measurement added successfully";
        }
        catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
}
