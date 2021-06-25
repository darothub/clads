package com.decagon.clads.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UploadImageDTO {
    private String fileId;
    private String fileType;
    private String fileName;
    private String downloadUri;
    private String description;
    private boolean uploadStatus;
}
