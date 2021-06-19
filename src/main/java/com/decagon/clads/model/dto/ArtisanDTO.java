package com.decagon.clads.model.dto;

import com.decagon.clads.entities.artisan.Address;
import com.decagon.clads.entities.artisan.Association;
import com.decagon.clads.entities.artisan.MeasurementOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtisanDTO {
    private Long id;
    private String firstName;
    private String lastName;
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


}
