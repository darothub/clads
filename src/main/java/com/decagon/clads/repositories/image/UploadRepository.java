package com.decagon.clads.repositories.image;

import com.decagon.clads.entities.image.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends JpaRepository<UploadImage, String> {
}
