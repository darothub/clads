package com.decagon.clads.repositories;

import com.decagon.clads.entities.artisan.Artisan;

import com.decagon.clads.entities.artisan.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AssociationRepository extends JpaRepository<Association, Long> {
    @Transactional
    @Modifying
    @Query("SELECT a FROM Association a WHERE a.artisan = ?1")
    Optional<Association> findByEmail(Artisan artisan);
}