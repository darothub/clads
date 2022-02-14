package com.decagon.clads.artisans.controllers;

import com.decagon.clads.artisans.entities.Artisan;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.artisans.services.auth.ProfileService;
import com.decagon.clads.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(path = ConstantUtils.BASE_URL)
public class ProfileController {
    private final SuccessResponseHandler successResponseHandler;
    private final ProfileService profileService;
    @GetMapping(path = "/me/profile")
    public ResponseEntity<ResponseModel> getArtisanProfile() {
        ArtisanDTO artisan = profileService.getArtisanProfile();
        return successResponseHandler.handleSuccessResponseEntity("Profile details", HttpStatus.OK, Optional.of(artisan));
    }

    @PutMapping(path = "/me/profile")
    public ResponseEntity<ResponseModel> updateArtisanProfile(@Valid @RequestBody Artisan artisan) {
        ArtisanDTO artisanDTO = profileService.updateArtisanProfile(artisan);
        return successResponseHandler.handleSuccessResponseEntity("Profile updated successfully", HttpStatus.OK, Optional.of(artisanDTO));
    }
}
