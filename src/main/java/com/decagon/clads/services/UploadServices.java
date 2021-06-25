package com.decagon.clads.services;

import com.decagon.clads.entities.image.UploadImage;
import com.decagon.clads.model.dto.UploadImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface UploadServices {
    public UploadImageDTO uploadImagesAndDescriptionToDb(MultipartFile file, String description) throws IOException;
    public Collection<UploadImageDTO> downloadImage() throws IOException;

    UploadImageDTO editUploadedImageDescription(String description, String fileId);

    void deleteUploadedImageDescription(String fileId);
}
