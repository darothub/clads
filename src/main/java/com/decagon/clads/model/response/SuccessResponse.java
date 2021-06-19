package com.decagon.clads.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@EqualsAndHashCode(callSuper = false)
public class SuccessResponse extends ResponseModel {
    private int status;
    private String message;
    private Object payload;
}
