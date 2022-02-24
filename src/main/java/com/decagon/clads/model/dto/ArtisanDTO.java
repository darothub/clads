package com.decagon.clads.model.dto;

import com.decagon.clads.artisans.entities.Address;
import com.decagon.clads.artisans.entities.Association;
import com.decagon.clads.artisans.entities.MeasurementOption;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtisanDTO {

    private String firstName;
    private String lastName;
    private String otherName;
    private String role;
    private String thumbnail;
    private String email;
    private String phoneNumber;
    private String gender;
    private String country;
    private Address workshopAddress;
    private Address showroomAddress;
    private Association union;
    private Set<String> specialties;
    private Set<String> genderFocus;
    private boolean trained = false;
    private MeasurementOption measurementOption = new MeasurementOption();
    private String deliveryTime;
    private Set<String> paymentTerms = new HashSet<>();
    private Set<String> paymentOptions = new HashSet<>();
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime createdAt = LocalDateTime.now();
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime updateAt = LocalDateTime.now();
}
