package org.example.presets.core.exception.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.presets.core.exception.ErrorCode;

@Getter
@AllArgsConstructor
public class CustomException extends IllegalArgumentException{
    private Domain domain;
    private Layer layer;
    private ErrorCode errorCode;
    private Object causeVariable;
}
