package org.example.presets.core.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.presets.core.exception.filter.jwt.CustomJwtException;
import org.example.presets.core.security.CustomUserDetails;
import org.example.presets.member.entity.MemberRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.example.presets.core.exception.filter.FilterErrorCode.EXPIRED_JWT;
import static org.example.presets.core.exception.filter.FilterErrorCode.INVALID_JWT;

@Slf4j
@Component
@PropertySource("classpath:jwt.properties")
@RequiredArgsConstructor
public class JwtUtil {
    public static final String HEADER_ACCESS = "AccessToken";
    public static final String HEADER_REFRESH = "RefreshToken";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String AUTHORIZATION_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 10 * 60 * 1000L;
    @Value("${secret.key.access}")
    private String accessTokenSecretKey;
    @Value("${secret.key.refresh}")
    private String refreshTokenSecretKey;

    private Key accessTokenKey;
    private Key refreshTokenKey;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct // 의존성 주입 이후 초기화를 수행하는 메소드. Bean Life Cycle 중 한 번만 실행되는 것을 강제
    public void init() {
        byte[] accessTokenBytes = Base64.getDecoder().decode(accessTokenSecretKey);
        accessTokenKey = Keys.hmacShaKeyFor(accessTokenBytes);

        byte[] refreshTokenBytes = Base64.getDecoder().decode(refreshTokenSecretKey);
        refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenBytes);
    }

    public String resolveToken(HttpServletRequest request, String authorization) {
        String bearerToken = request.getHeader(authorization);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String createAccessToken(Long memberId, MemberRole role) {
        Date date = new Date();
        return AUTHORIZATION_PREFIX +
                Jwts.builder()
                        .setSubject(memberId.toString())
                        .claim(AUTHORIZATION_KEY, role.name())
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(accessTokenKey, signatureAlgorithm)
                        .compact();
    }

    public String createRefreshToken(Long memberId) {
        Date date = new Date();
        return AUTHORIZATION_PREFIX +
                Jwts.builder()
                        .setSubject(memberId.toString())
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME + 30 * 60 * 1000L))
                        .setIssuedAt(date)
                        .signWith(refreshTokenKey, signatureAlgorithm)
                        .compact();
    }

    public Claims getLoginMemberInfoFromHttpServletRequest(HttpServletRequest request, Boolean isAccess) {
        try {
            if (isAccess) {
                return Jwts.parserBuilder()
                        .setSigningKey(accessTokenKey).build()
                        .parseClaimsJws(resolveToken(request, HEADER_ACCESS)).getBody();
            }
            return Jwts.parserBuilder()
                    .setSigningKey(refreshTokenKey).build()
                    .parseClaimsJws(resolveToken(request, HEADER_REFRESH)).getBody();
        } catch (SecurityException | MalformedJwtException e) {
            throw new CustomJwtException(INVALID_JWT);
        } catch (ExpiredJwtException e) {
            throw new CustomJwtException(EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT, 지원되지 않는 Refresh JWT 입니다.");
        }
        throw new CustomJwtException(INVALID_JWT);
    }

    public Authentication createAuthentication(Long memberId, MemberRole memberRole) {
        UserDetails userDetails = new CustomUserDetails(memberId, memberRole);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}