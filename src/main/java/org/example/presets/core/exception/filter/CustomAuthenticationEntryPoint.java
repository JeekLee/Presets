package org.example.presets.core.exception.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import static org.example.presets.core.exception.ErrorCode.ACCESSTOKEN_NOT_EXIST;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException
    ) throws CustomJwtException {
        throw new CustomJwtException(ACCESSTOKEN_NOT_EXIST);
    }
}
