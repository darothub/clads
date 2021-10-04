package com.decagon.clads.services.client;

import com.decagon.clads.entities.artisan.Address;
import com.decagon.clads.entities.client.Client;
import com.decagon.clads.entities.client.Measurement;
import com.decagon.clads.exceptions.CustomException;
import com.decagon.clads.filter.JwtFilter;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.dto.ClientDTO;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.repositories.client.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.hibernate.proxy.HibernateProxy;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final ErrorResponse errorResponse;
    public Client addClient(Client client){
        Collection<Client> isOldClientWithPhoneNumberAndEmail = clientRepository.findClientByPhoneNumberAndEmail(client.getPhoneNumber(), client.getEmail(), JwtFilter.userId);
        log.info("Existed number {}", isOldClientWithPhoneNumberAndEmail);
        if (!isOldClientWithPhoneNumberAndEmail.isEmpty()){
            throw new IllegalStateException("Client already exist");
        }
        client.setArtisanId(JwtFilter.userId);
        client.setCreatedAt(LocalDateTime.now());
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
            client.setMeasurements(measurement);
            clientRepository.save(client);
            return "Measurement added successfully";
        }
        catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
    public Client getSingleClient(String id) {
        long clientId = Integer.parseInt(id);
        Optional<Client> client =  clientRepository.findById(clientId);
        if (client.isEmpty()){
            errorResponse.setMessage("Client with "+id+" is not found");
            errorResponse.setStatus(HttpStatus.SC_NOT_FOUND);
            throw new CustomException(errorResponse);
        }
        return client.get();
    }

    public String deleteClientById(String id) {
        Client client = getSingleClient(id);
        clientRepository.delete(client);
        return "Client deleted successfully";
    }

    public Client editClientById(Client client, String id) {
       try{
           long clientId =  Integer.parseInt(id);
           client.setId(clientId);
           Collection<Client> isOldClientWithPhoneNumberAndEmail = clientRepository.findClientByPhoneNumberAndEmail(client.getPhoneNumber(), client.getEmail(), JwtFilter.userId);
           if(isOldClientWithPhoneNumberAndEmail.size() > 1){
               errorResponse.setMessage("Incoming details has duplicates with another client, see administrator");
               errorResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
               throw new CustomException(errorResponse);
           }
           client.setUpdateAt(LocalDateTime.now());
           return clientRepository.save(client);
       }
       catch (Exception e){
           errorResponse.setMessage(e.getMessage());
           errorResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
           throw new CustomException(errorResponse);
       }
    }
}
