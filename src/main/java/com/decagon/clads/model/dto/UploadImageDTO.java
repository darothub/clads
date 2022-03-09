package com.decagon.clads.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadImageDTO {
    private String fileId;
    private String fileType;
    private String fileName;
    private String downloadUri;
    private String description;
    private boolean uploadStatus;
}
