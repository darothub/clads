package com.decagon.clads.repositories;


import com.decagon.clads.entities.Artisan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ArtisanRepository extends JpaRepository<Artisan, Long> {
    Optional<Artisan> findByEmail(String email);
}
