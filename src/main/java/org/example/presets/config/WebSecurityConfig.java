package org.example.presets.config;

import lombok.RequiredArgsConstructor;
import org.example.presets.core.exception.filter.CustomAccessDeniedHandler;
import org.example.presets.core.exception.filter.CustomAuthenticationEntryPoint;
import org.example.presets.core.exception.filter.jwt.CustomJwtExceptionHandlerFilter;
import org.example.presets.core.security.jwt.JwtAuthFilter;
import org.example.presets.core.security.jwt.JwtUtil;
import org.example.presets.member.entity.MemberRole;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.Arrays;

import static org.example.presets.core.security.jwt.JwtUtil.HEADER_ACCESS;
import static org.example.presets.core.security.jwt.JwtUtil.HEADER_REFRESH;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;

    // Set BCryptPasswordEncoder as Password Encoder
    // BCryptPasswordEncoder uses SHA-2(Secure Hash Algorithm) and random salts
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                // Ignore security for H2 Console
                .requestMatchers(PathRequest.toH2Console())
                // Ignore security for StaticResources of Spring Boot
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow CORS origin of FE server
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH","DELETE", "PUT", "OPTIONS"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        // Expose headers for JWT
        configuration.addExposedHeader(HEADER_ACCESS);
        configuration.addExposedHeader(HEADER_REFRESH);

        // Adjust CORS conditions
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Set CustomAccessDeniedHandler as AccessDeniedHandler
    // CustomAccessDeniedHandler throws CustomJwtException(ACCESS_DENIED)
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }


    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(exceptionHandling
                        -> exceptionHandling.accessDeniedHandler(accessDeniedHandler())
                                .authenticationEntryPoint(authenticationEntryPoint())
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/member/login", "/member/signup", "/member/reissuance").permitAll()
                                .requestMatchers("/admin/**").hasRole(MemberRole.ADMIN.getCode())
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomJwtExceptionHandlerFilter(), JwtAuthFilter.class);

        return http.build();
    }

}
