package com.decagon.clads.upload.repositories;

import com.decagon.clads.upload.entities.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface UploadRepository extends JpaRepository<UploadImage, String> {
    @Transactional
    @Modifying
    @Query("SELECT u FROM UploadImage u where u.userId = ?1")
    Collection<UploadImage> findUploadImageByUserId(Long artisanId);
    UploadImage findByFileId(String fileId);
}
