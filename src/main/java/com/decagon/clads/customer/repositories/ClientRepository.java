package com.decagon.clads.customer.repositories;

import com.decagon.clads.artisans.entities.Artisan;
import com.decagon.clads.customer.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    @Transactional
    @Modifying
    @Query("SELECT c FROM Client c where ?1 IN c.artisanId ")
    Collection<Client> getClientsByArtisanId(Long id);
    @Transactional
    @Modifying
    @Query("UPDATE Client a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableCustomer(String email);
    @Transactional
    @Modifying
    @Query("SELECT c FROM Client c where c.email = ?2 or c.phoneNumber = ?1 ")
    Optional<Client> findClientByPhoneNumberAndEmail(String phoneNumber, String email);
}
