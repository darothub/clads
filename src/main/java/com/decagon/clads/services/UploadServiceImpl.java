package com.decagon.clads.services;

import com.decagon.clads.entities.image.UploadImage;
import com.decagon.clads.filter.JwtFilter;
import com.decagon.clads.jwt.JWTUtility;
import com.decagon.clads.model.dto.UploadImageDTO;
import com.decagon.clads.model.request.ImageAndDescription;
import com.decagon.clads.repositories.image.UploadRepository;
import com.decagon.clads.utils.ConstantUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Slf4j
@Service
public class UploadServiceImpl implements UploadServices{
    private final UploadRepository uploadRepository;
    private final ObjectMapper objectMapper;
//    @Override
//    public List<UploadImageDTO> uploadImagesToDb(MultipartFile[] files) throws IOException {
//        List<UploadImageDTO> uploadedImages = new ArrayList<>();
//        Arrays.stream(files).forEach(file -> {
//            if(file != null && !Objects.requireNonNull(file.getContentType()).matches(ConstantUtils.IMAGE_PATTERN)){
//                throw new IllegalStateException("Invalid image format");
//            }
//            UploadImage uploadImage = new UploadImage();
//            try {
//                uploadImage.setFileData(file.getBytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            uploadImage.setFileType(file.getContentType());
//            uploadImage.setFileName(file.getOriginalFilename());
//            uploadImage.setUserId(JwtFilter.userId);
//            UploadImage savedImage = uploadRepository.save(uploadImage);
//
//            UploadImageDTO imageUploadDTO = new UploadImageDTO();
//            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                    .path("/api/v1/download/images")
//                    .path(savedImage.getFileId())
//                    .toUriString();
//            imageUploadDTO.setDownloadUri(uri);
//            imageUploadDTO.setFileId(savedImage.getFileId());
//            imageUploadDTO.setFileName(savedImage.getFileName());
//            imageUploadDTO.setFileType(savedImage.getFileType());
//            imageUploadDTO.setUploadStatus(true);
//            uploadedImages.add(imageUploadDTO);
//
//        });
//        return uploadedImages;
//    }
    @Override
    public UploadImageDTO uploadImagesAndDescriptionToDb(MultipartFile file, String description) throws IOException {

        if(file != null && !Objects.requireNonNull(file.getContentType()).matches(ConstantUtils.IMAGE_PATTERN)){
            throw new IllegalStateException("Invalid image format");
        }
        UploadImage uploadImage = new UploadImage();
        try {
            assert file != null;
            uploadImage.setFileData(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        uploadImage.setFileType(file.getContentType());
        uploadImage.setFileName(file.getOriginalFilename());
        uploadImage.setUserId(JwtFilter.userId);
        uploadImage.setDescription(description);
        UploadImage savedImage = uploadRepository.save(uploadImage);
        log.info("Upload image1 {}", uploadImage);
        UploadImageDTO imageUploadDTO = objectMapper.convertValue(savedImage, UploadImageDTO.class);
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/download/image/")
                .path(savedImage.getFileId())
                .toUriString();
        imageUploadDTO.setDownloadUri(uri);
        imageUploadDTO.setUploadStatus(true);
        return imageUploadDTO;
    }

    @Override
    public Collection<UploadImageDTO> downloadImage() throws IOException {
        Collection<UploadImage> listOfImages = uploadRepository.findUploadImageByUserId(JwtFilter.userId);
        return listOfImages.stream().map(this::getImageDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UploadImage downloadImage(String id) throws IOException {
        return uploadRepository.getById(id);
    }

    @Transactional
    @Override
    public UploadImageDTO editUploadedImageDescription(String description, String fileId) {
        UploadImage uploadImage = uploadRepository.getById(fileId);
        uploadImage.setDescription(description);
        UploadImage newImage = uploadRepository.save(uploadImage);
        UploadImageDTO imageUploadDTO = objectMapper.convertValue(newImage, UploadImageDTO.class);
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/download/image/")
                .path(uploadImage.getFileId())
                .toUriString();
        imageUploadDTO.setDownloadUri(uri);
        imageUploadDTO.setUploadStatus(true);
        return imageUploadDTO;
    }

    @Override
    public void deleteUploadedImageDescription(String fileId) {
        uploadRepository.deleteById(fileId);
    }

    public UploadImageDTO getImageDto(UploadImage image){
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/download/image/")
                .path(image.getFileId())
                .toUriString();
        UploadImageDTO imageUploadDTO = objectMapper.convertValue(image, UploadImageDTO.class);
        imageUploadDTO.setDownloadUri(uri);
        imageUploadDTO.setUploadStatus(true);
        return imageUploadDTO;
    }
}
