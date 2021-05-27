package com.decagon.clads.entities.artisan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {
    @Id
    @SequenceGenerator(name = "address_sequence", sequenceName = "address_sequence", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="address_sequence")
    private Long id;
    private String street ="";
    private String city ="";
    private String state="";
//    @OneToOne
//    @JoinColumns({
//            @JoinColumn(name="artisan_id", referencedColumnName="id"),
//            @JoinColumn(name="artisan_email", referencedColumnName="email")
//    })
//    private Artisan artisan;
    private String type;

    public Address(String type){
        this.type = type;
    }
}
