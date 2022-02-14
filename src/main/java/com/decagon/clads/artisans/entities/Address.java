package com.decagon.clads.artisans.entities;

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
    private String city;
    private String state;
    private String longitude;
    private String latitude;
}
