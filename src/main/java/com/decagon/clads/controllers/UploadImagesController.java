package com.decagon.clads.controllers;

import com.decagon.clads.model.dto.UploadImageDTO;
import com.decagon.clads.model.request.ImageAndDescription;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.services.UploadServiceImpl;
import com.decagon.clads.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

//    @PostMapping("/upload")
//    public ResponseEntity<ResponseModel> uploadToDb(@RequestParam("files") MultipartFile[] files) throws IOException {
//        List<UploadImageDTO> imageUploadDTO =  uploadService.uploadImagesToDb(files);
//        return successResponseHandler.handleSuccessResponseEntity("Image uploaded successfully", HttpStatus.CREATED, imageUploadDTO);
//    }
    @PostMapping("/upload")
    public ResponseEntity<ResponseModel> uploadImageAndDescriptionToDb(@RequestPart("file") MultipartFile file, @RequestPart("description") String imageAndDescription) throws IOException {
        UploadImageDTO imageUploadDTO =  uploadService.uploadImagesAndDescriptionToDb(file, imageAndDescription);
        return successResponseHandler.handleSuccessResponseEntity("Image uploaded successfully", HttpStatus.CREATED, imageUploadDTO);
    }
    @PatchMapping("/upload/{fileId}")
    public ResponseEntity<ResponseModel> editUploadedImageDescription(@RequestParam("description") String description, @PathVariable String fileId) throws IOException {
        UploadImageDTO imageUploadDTO =  uploadService.editUploadedImageDescription(description, fileId);
        return successResponseHandler.handleSuccessResponseEntity("Image edited successfully", HttpStatus.OK, imageUploadDTO);
    }
    @DeleteMapping("/upload/{fileId}")
    public ResponseEntity<ResponseModel> deleteUploadedImageDescription(@PathVariable String fileId) throws IOException {
        uploadService.deleteUploadedImageDescription(fileId);
        return successResponseHandler.handleSuccessResponseEntity("Image deleted successfully", HttpStatus.OK, null);
    }
    @GetMapping("/download/images")
    public ResponseEntity<ResponseModel> downloadFile() throws IOException {
        Collection<UploadImageDTO> uploadImage = uploadService.downloadImage();
        return successResponseHandler.handleSuccessResponseEntity("Images", HttpStatus.OK, uploadImage);
    }


}
