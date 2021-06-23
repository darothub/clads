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
@EqualsAndHashCode(callSuper = false)
public class UploadImageDTO {
    private String fileId;
    private String fileType;
    private String fileName;
    private String downloadUri;
    private boolean uploadStatus;
}
