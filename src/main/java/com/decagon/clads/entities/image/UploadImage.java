package com.decagon.clads.entities.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadImage {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String fileId;
    private String fileType;
    private String fileName;
    private Long userId;
    @Lob
    private byte[] fileData;
}
