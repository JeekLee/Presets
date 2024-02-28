package org.example.presets.core.exception.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.presets.core.exception.ExceptionResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.OutputStream;

import static org.example.presets.core.exception.filter.FilterErrorCode.ACCESS_DENIED;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
            HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException
    ) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(ACCESS_DENIED.getHttpStatus().value());
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, ExceptionResponse.builder()
                    .customHttpStatus(ACCESS_DENIED.getCustomHttpStatusCode())
                    .message(ACCESS_DENIED.getMessage())
                    .build()
            );
            os.flush();
        }
    }
}
