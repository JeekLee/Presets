package org.example.presets.core.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.presets.member.entity.MemberRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.example.presets.core.security.JwtUtil.AUTHORIZATION_KEY;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    private void setAuthentication(Claims loginMemberInfo) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(
                Long.parseLong(loginMemberInfo.getSubject()),
                MemberRole.valueOf((String) loginMemberInfo.get(AUTHORIZATION_KEY))
        );
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtUtil.resolveToken(request, "AccessToken");

        if (token == null || request.getRequestURI().equals("/api/member/reissuance")){
            filterChain.doFilter(request, response);
            return;
        }

        setAuthentication(jwtUtil.getLoginMemberInfoFromHttpServletRequest(request, true));

        filterChain.doFilter(request,response);
    }
}
