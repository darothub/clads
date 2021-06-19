package com.decagon.clads.entities.artisan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address  {
    @NotEmpty
    @Size(min=3)
    private String street;
    @NotEmpty
    @Size(min=3)
    private String city;
    @NotEmpty
    @Size(min=3)
    private String state;
}
