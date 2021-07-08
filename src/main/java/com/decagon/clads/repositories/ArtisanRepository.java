package com.decagon.clads.repositories;


import com.decagon.clads.entities.artisan.Artisan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ArtisanRepository extends JpaRepository<Artisan, Long> {
    Optional<Artisan> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Artisan a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableArtisan(String email);
    @Transactional
    @Modifying
    @Query("UPDATE Artisan a " +
            "SET a = ?1 WHERE a.email = ?2")
    int updateArtisan(Artisan artisan, String email);

}
