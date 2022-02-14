package com.decagon.clads.customer.repositories;

import com.decagon.clads.customer.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
@Transactional(readOnly = true)
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Transactional
    @Modifying
    @Query("SELECT c FROM Client c where c.artisanId = ?1 ")
    Collection<Client> getClientsByArtisanId(Long id);

    @Transactional
    @Modifying
    @Query("SELECT c FROM Client c where c.artisanId = ?3 and c.email = ?2 or c.phoneNumber = ?1 ")
    Collection<Client> findClientByPhoneNumberAndEmail(String phoneNumber, String email, Long artisanId);
}
