package com.decagon.clads.artisans.services.auth;

import com.decagon.clads.artisans.entities.Artisan;
import com.decagon.clads.model.dto.ArtisanDTO;

public interface ArtisanProfileService {
    public ArtisanDTO getArtisanProfile();
    public ArtisanDTO updateArtisanProfile(Artisan artisan);
}
