package com.decagon.clads.customer.entities;

import com.decagon.clads.artisans.entities.Address;
import com.decagon.clads.artisans.entities.Association;
import com.decagon.clads.artisans.entities.Measurement;
import com.decagon.clads.artisans.entities.MeasurementOption;
import com.decagon.clads.utils.AUTHPROVIDER;
import com.decagon.clads.utils.ConstantUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String gender;
    private Set<Address> deliveryAddresses = new HashSet<>();
    private Set<Measurement> measurements = new HashSet<>();
    private AUTHPROVIDER authprovider = AUTHPROVIDER.REGULAR;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime createdAt = LocalDateTime.now();
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime updateAt = LocalDateTime.now();
}
