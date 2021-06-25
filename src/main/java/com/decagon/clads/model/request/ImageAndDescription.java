package com.decagon.clads.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageAndDescription {
    private MultipartFile file;
    private String description;
}
