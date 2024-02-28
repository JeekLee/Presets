package org.example.presets.core.exception.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.example.presets.core.exception.ExceptionResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.OutputStream;

import static org.example.presets.core.exception.filter.FilterErrorCode.UNAUTHORIZED_USER;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.warn("User : " + authentication.getPrincipal() + "attempted to access protected URL " + request.getRequestURI());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED_USER.getHttpStatus().value());
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, ExceptionResponse.builder()
                    .customHttpStatus(UNAUTHORIZED_USER.getCustomHttpStatusCode())
                    .message(UNAUTHORIZED_USER.getMessage())
                    .build()
            );
            os.flush();
        }
    }
}
