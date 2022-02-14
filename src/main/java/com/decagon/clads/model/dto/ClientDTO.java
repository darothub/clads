package com.decagon.clads.model.dto;

import com.decagon.clads.artisans.entities.Address;
import com.decagon.clads.artisans.entities.Measurement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDTO implements Serializable {
    private Long id;
    private Long artisanId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String gender;
    private Set<Address> deliveryAddresses;
    private Set<Measurement> measurements;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
