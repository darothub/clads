package com.decagon.clads.services;

import com.decagon.clads.entities.image.UploadImage;
import com.decagon.clads.filter.JwtFilter;
import com.decagon.clads.jwt.JWTUtility;
import com.decagon.clads.model.dto.UploadImageDTO;
import com.decagon.clads.repositories.image.UploadRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Slf4j
@Service
public class UploadServiceImpl implements UploadServices{
    private final UploadRepository uploadRepository;
    @Override
    public List<UploadImageDTO> uploadToDb(MultipartFile files) throws IOException {
        List<UploadImageDTO> uploadedImages = new ArrayList<>();
        Stream.of(files).forEach(file -> {
            UploadImage uploadImage = new UploadImage();
            try {
                uploadImage.setFileData(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadImage.setFileType(file.getContentType());
            uploadImage.setFileName(file.getOriginalFilename());
            uploadImage.setUserId(JwtFilter.userId);
            UploadImage savedImage = uploadRepository.save(uploadImage);

            UploadImageDTO imageUploadDTO = new UploadImageDTO();
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/download/images")
                    .path(savedImage.getFileId())
                    .toUriString();
            imageUploadDTO.setDownloadUri(uri);
            imageUploadDTO.setFileId(savedImage.getFileId());
            imageUploadDTO.setFileName(savedImage.getFileName());
            imageUploadDTO.setFileType(savedImage.getFileType());
            imageUploadDTO.setUploadStatus(true);
            uploadedImages.add(imageUploadDTO);

        });
        return uploadedImages;
    }

    @Override
    public Collection<UploadImageDTO> downloadImage() throws IOException {
        Collection<UploadImage> listOfImages = uploadRepository.findUploadImageByUserId(JwtFilter.userId);

        return listOfImages.stream().map(this::getImageDto).collect(Collectors.toList());

    }

    public UploadImageDTO getImageDto(UploadImage image){
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/download/images/")
                .path(image.getFileId())
                .toUriString();
        UploadImageDTO imageUploadDTO = new UploadImageDTO();
        imageUploadDTO.setDownloadUri(uri);
        imageUploadDTO.setFileId(image.getFileId());
        imageUploadDTO.setFileName(image.getFileName());
        imageUploadDTO.setFileType(image.getFileType());
        imageUploadDTO.setUploadStatus(true);
        return imageUploadDTO;
    }
}
