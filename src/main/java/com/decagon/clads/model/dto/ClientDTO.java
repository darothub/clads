package com.decagon.clads.model.dto;

import com.decagon.clads.entities.artisan.Address;
import com.decagon.clads.entities.client.Measurement;
import com.decagon.clads.utils.ConstantUtils;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO implements Serializable {
    private Long id;
    private Long artisanId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String gender;
    private Set<Address> deliveryAddresses;
    private Set<Measurement> measurements;
}
