package com.decagon.clads.artisans.services.auth;

import com.decagon.clads.artisans.entities.Artisan;
import com.decagon.clads.filter.JwtFilter;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.artisans.repositories.ArtisanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ProfileService {
    private final ArtisanRepository artisanRepository;
    private final ModelMapper modelMapper;
    private final ErrorResponse errorResponse;

    public ArtisanDTO getArtisanProfile() {
        errorResponse.setPayload("Profile service error");
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage("User not found");

        log.info("Email {}",  JwtFilter.userName);
        Artisan artisan = getArtisan();
        return modelMapper.map(artisan, ArtisanDTO.class);
    } 

    @Cacheable
    public Artisan getArtisan() {
        return artisanRepository.findByEmail(JwtFilter.userName)
                    .orElseThrow(()-> new IllegalStateException("User not found"));
    }

    public ArtisanDTO updateArtisanProfile(Artisan artisan) {
        log.info("Email {}", JwtFilter.userName);
        Artisan artisan1 = getArtisan();
        artisan.setPassword(artisan1.getPassword());
        artisan.setEnabled(artisan1.isEnabled());
        artisan.setId(artisan1.getId());
        artisan.setAuthprovider(artisan1.getAuthprovider());
        artisanRepository.save(artisan);
        log.info("Artisan {}", artisan1);
        return getArtisanProfile();
    }
}
