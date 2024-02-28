package org.example.presets.core.exception.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotFoundException extends IllegalArgumentException{
    private Domain domain;
    private Layer layer;
    private GlobalErrorCode globalErrorCode;
    private Object causeVariable;
}
