package com.decagon.clads.artisans.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Embeddable
public class Association {
    @NotEmpty
    @Size(min=3)
    private String name;
    @NotEmpty
    private String ward;
    @NotEmpty
    private String lga;
    @NotEmpty
    private String state;
}
