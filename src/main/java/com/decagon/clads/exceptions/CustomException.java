package com.decagon.clads.exceptions;

import com.decagon.clads.model.response.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomException extends RuntimeException {
    /**
     * For serialization: if any changes is made to this class, update the
     * serialversionID
     */
    private static final long serialVersionUID = 1L;
    private ErrorResponse error;

}

