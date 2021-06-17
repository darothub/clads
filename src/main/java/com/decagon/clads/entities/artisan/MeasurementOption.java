package com.decagon.clads.entities.artisan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode(callSuper = false)
public class MeasurementOption {
    private boolean visitForMeasurement = false;
    private boolean acceptSelfMeasurement = false;
}
