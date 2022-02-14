package com.decagon.clads.upload.services;

import com.decagon.clads.upload.entities.UploadImage;
import com.decagon.clads.model.dto.UploadImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

public interface UploadServices {
    public UploadImageDTO uploadImagesAndDescriptionToDb(MultipartFile file, String description) throws IOException;
    public Collection<UploadImageDTO> downloadImage() throws IOException;

    public UploadImage downloadImage(String id) throws IOException;

    UploadImageDTO editUploadedImageDescription(String description, String fileId);

    void deleteUploadedImageDescription(String fileId);
}
