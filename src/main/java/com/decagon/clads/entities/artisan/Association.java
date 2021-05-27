package com.decagon.clads.entities.artisan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Primary
public class Association {
    @Id
    @SequenceGenerator(name = "association_sequence", sequenceName = "association_sequence", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="association_sequence")
    private Long id;
    private String name;
    private String ward;
    private String lga;
    private String state;
    @OneToOne
    @JoinColumns({
            @JoinColumn(name="artisan_id", referencedColumnName="id"),
            @JoinColumn(name="artisan_email", referencedColumnName="email")
    })
    private Artisan artisan;

    public Association(String name, String ward, String lga, String state, Artisan artisan){
        this.name = name;
        this.ward = ward;
        this.lga = lga;
        this.state = state;
        this.artisan = artisan;
    }
    public Association(Artisan artisan){
        this.artisan = artisan;
    }
}
