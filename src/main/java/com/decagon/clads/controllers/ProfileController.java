package com.decagon.clads.controllers;

import com.decagon.clads.entities.artisan.Artisan;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.services.ProfileService;
import com.decagon.clads.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
@RequestMapping(path = ConstantUtils.BASE_URL)
public class ProfileController {
    private final SuccessResponseHandler successResponseHandler;
    private final ProfileService profileService;
    @GetMapping(path = "/me/profile")
    public ResponseEntity<ResponseModel> getArtisanProfile() {
        ArtisanDTO artisan = profileService.getArtisanProfile();
        return successResponseHandler.handleSuccessResponseEntity("Profile details", HttpStatus.OK, artisan);
    }

    @PutMapping(path = "/me/profile")
    public ResponseEntity<ResponseModel> updateArtisanProfile(@Valid @RequestBody Artisan artisan) {
        ArtisanDTO artisanDTO = profileService.updateArtisanProfile(artisan);
        return successResponseHandler.handleSuccessResponseEntity("Profile updated successfully", HttpStatus.OK, artisanDTO);
    }
}
