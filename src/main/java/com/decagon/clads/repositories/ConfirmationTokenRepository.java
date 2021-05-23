package com.decagon.clads.repositories;

import com.decagon.clads.entities.Artisan;
import com.decagon.clads.entities.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime now);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.createdAt = ?2, c.expiresAt = ?3, c.token = ?4 " +
            "WHERE c.artisan = ?1"
    )
    int updateConfirmationToken(Artisan artisan, LocalDateTime createdAt, LocalDateTime expiresAt, String token);
}
