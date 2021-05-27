package com.decagon.clads.model.dto;

import com.decagon.clads.entities.artisan.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtisanDTO {
    private String firstName;
    private String lastName;
    private String role;
    private String thumbnail;
    private String email;
    private String phoneNumber;
    private String gender;
    private String country;
}
