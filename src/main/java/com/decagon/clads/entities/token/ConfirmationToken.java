package com.decagon.clads.entities.token;

import com.decagon.clads.entities.artisan.Artisan;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonSerialize(using = LocalDateTimeSerializer.class)
public class ConfirmationToken {

    @Id
    @SequenceGenerator(name = "confirmation_token_sequence", sequenceName = "confirmation_token_sequence", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="confirmation_token_sequence")
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    @OneToOne
    @JoinColumns({
            @JoinColumn(name="artisan_id", referencedColumnName="id"),
            @JoinColumn(name="artisan_email", referencedColumnName="email")
    })
    private Artisan artisan;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, Artisan artisan){
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.artisan = artisan;
    }
}
