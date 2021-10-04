package com.decagon.clads.entities.client;

import com.decagon.clads.entities.artisan.Address;
import com.decagon.clads.utils.ConstantUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client  {
    @Id
    @SequenceGenerator(name = "client_sequence", sequenceName = "client_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_sequence")
    private Long id;
    private Long artisanId;
    @NotNull
    @NotBlank
    private String fullName;
    @Pattern(regexp = ConstantUtils.PHONE_NUMBER_PATTERN, message = "Invalid phone number")
    private String phoneNumber;
    @NotBlank
    @NotNull
    @Email
    private String email;
    @Pattern(regexp = ConstantUtils.GENDER_PATTERN, message = "Invalid gender type")
    private String gender;
    @ElementCollection
    @Valid
    @NotEmpty
    private Set<Address> deliveryAddresses = new HashSet<>();
    @ElementCollection
    @Valid
    @NotEmpty
    private Set<Measurement> measurements = new HashSet<>();
    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
