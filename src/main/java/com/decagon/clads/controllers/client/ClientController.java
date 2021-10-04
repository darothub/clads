package com.decagon.clads.controllers.client;

import com.decagon.clads.controllers.SuccessResponseHandler;
import com.decagon.clads.entities.artisan.Address;
import com.decagon.clads.entities.client.Client;
import com.decagon.clads.entities.client.Measurement;
import com.decagon.clads.model.dto.ClientDTO;
import com.decagon.clads.model.request.LoginRequest;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.services.LoginService;
import com.decagon.clads.services.client.ClientService;
import com.decagon.clads.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping(path = ConstantUtils.BASE_URL)
@Slf4j
public class ClientController {
    private final SuccessResponseHandler successResponseHandler;
    private final ClientService clientService;

    @PostMapping(path = "/client")
    public ResponseEntity<ResponseModel> addClient(@Valid @RequestBody Client client) {
        Client client1 = clientService.addClient(client);
        return successResponseHandler.handleSuccessResponseEntity("Client added successfully", HttpStatus.OK, Optional.of(client1));
    }

    @GetMapping(path = "/clients")
    public ResponseEntity<ResponseModel> getClients() {
        Collection<Client> clients = clientService.getClients();
        return successResponseHandler.handleSuccessResponseEntity("Clients", HttpStatus.OK, Optional.of(clients));
    }
    @GetMapping(path = "/client/{id}")
    public ResponseEntity<ResponseModel> getClientById(@PathVariable String id)  {

        Client client = clientService.getSingleClient(id);
        return successResponseHandler.handleSuccessResponseEntity("Client", HttpStatus.OK, Optional.of(client));
    }
    @DeleteMapping(path = "/client/{id}")
    public ResponseEntity<ResponseModel> deleteClientById(@PathVariable String id)  {
        String resp = clientService.deleteClientById(id);
        return successResponseHandler.handleSuccessResponseEntity(resp, HttpStatus.OK);
    }
    @PutMapping(path = "/client/{id}")
    public ResponseEntity<ResponseModel> editClientById(@Valid @RequestBody Client client, @PathVariable String id)  {
        Client client1 = clientService.editClientById(client,id);
        return successResponseHandler.handleSuccessResponseEntity("Client details updated", HttpStatus.OK, Optional.of(client1));
    }
    @PostMapping(path = "/client/{id}/delivery-address")
    public ResponseEntity<ResponseModel> addDeliveryAddress(@Valid @RequestBody Address address, @PathVariable String id) {
        String response = clientService.addDeliveryAddress(address, id);
        return successResponseHandler.handleSuccessResponseEntity(response, HttpStatus.OK, Optional.empty());
    }
    @PostMapping(path = "/client/{id}/measurements")
    public ResponseEntity<ResponseModel> addMeasurements(@Valid @RequestBody Set<Measurement> measurement, @PathVariable String id) {
        String response = clientService.addMeasurements(measurement, id);
        return successResponseHandler.handleSuccessResponseEntity(response, HttpStatus.OK, Optional.empty());
    }
}
