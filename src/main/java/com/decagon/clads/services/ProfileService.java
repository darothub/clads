package com.decagon.clads.services;

import com.decagon.clads.entities.artisan.Artisan;
import com.decagon.clads.exceptions.CustomException;
import com.decagon.clads.filter.JwtFilter;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.repositories.ArtisanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ProfileService {
    private final ArtisanRepository artisanRepository;
    private final ModelMapper modelMapper;
    private final ErrorResponse errorResponse;
    public ArtisanDTO getArtisanProfile() {
        errorResponse.setError("Profile service error");
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage("User not found");

        log.info("Email {}",  JwtFilter.userName);
        Artisan artisan = artisanRepository.findByEmail(JwtFilter.userName)
                .orElseThrow(()-> new CustomException(errorResponse));
        return modelMapper.map(artisan, ArtisanDTO.class);
    }
    public ArtisanDTO updateArtisanProfile(Artisan artisan) {
        log.info("Email {}", JwtFilter.userName);
        Artisan artisan1 = artisanRepository.findByEmail(JwtFilter.userName)
                .orElseThrow(()-> new CustomException(errorResponse));
        artisan.setPassword(artisan1.getPassword());
        artisanRepository.save(artisan);
        log.info("Artisan {}", artisan1);
        return getArtisanProfile();
    }
}
