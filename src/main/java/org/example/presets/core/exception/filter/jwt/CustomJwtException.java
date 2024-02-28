package org.example.presets.core.exception.filter.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.example.presets.core.exception.filter.FilterErrorCode;

@Getter
@AllArgsConstructor
public class CustomJwtException extends SecurityException{
    private final FilterErrorCode filterErrorCode;
}
