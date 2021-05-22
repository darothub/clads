package com.decagon.clads.services;

import com.decagon.clads.entities.Artisan;
import com.decagon.clads.exceptions.CustomException;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.repositories.ArtisanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class RegistrationService {

    private final ArtisanRepository artisanRepository;
    private final ModelMapper modelMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public ArtisanDTO register(Artisan artisan) {
        Optional<Artisan> artisanExists = artisanRepository.findByEmail(artisan.getEmail());
        if (artisanExists.isEmpty()) {
            String encodedPassword = bCryptPasswordEncoder.encode(artisan.getPassword());
            artisan.setPassword(encodedPassword);
            Artisan newArtisan = artisanRepository.save(artisan);
            return modelMapper.map(newArtisan, ArtisanDTO.class);
        } else {
            ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.toString(), String.format("%s already taken", artisan.getEmail()));
            throw new CustomException(error);
        }
    }
}
