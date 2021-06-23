package com.decagon.clads.controllers;

import com.decagon.clads.entities.image.UploadImage;
import com.decagon.clads.model.dto.UploadImageDTO;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.services.UploadServiceImpl;
import com.decagon.clads.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping(path = ConstantUtils.BASE_URL)
public class UploadImagesController {
    private final UploadServiceImpl uploadService;
    private final SuccessResponseHandler successResponseHandler;

    @PostMapping("/upload")
    public ResponseEntity<ResponseModel> uploadToDb(@RequestParam("files") MultipartFile files) throws IOException {
        if(files != null && Objects.requireNonNull(files.getContentType()).matches(ConstantUtils.IMAGE_PATTERN)){
            List<UploadImageDTO> imageUploadDTO =  uploadService.uploadToDb(files);
            return successResponseHandler.handleSuccessResponseEntity("Image uploaded successfully", HttpStatus.OK, imageUploadDTO);
        }
        return successResponseHandler.handleSuccessResponseEntity("Invalid image format", HttpStatus.BAD_REQUEST, null);

    }
    @GetMapping("/download/images")
    public ResponseEntity<ResponseModel> downloadFile() throws IOException {
        Collection<UploadImageDTO> uploadImage = uploadService.downloadImage();
        return successResponseHandler.handleSuccessResponseEntity("Images", HttpStatus.OK, uploadImage);
    }
}
