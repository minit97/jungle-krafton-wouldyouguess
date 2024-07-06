package com.krafton.api_server.config;

import com.krafton.api_server.api.auth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final KakaoService kakaoService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/login/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(kakaoService)
                        )
                        .successHandler((request, response, authentication) -> {
                            // 인증 성공 후 처리
                            response.sendRedirect("/loginSuccess");
                        })
                        .failureHandler((request, response, exception) -> {
                            // 인증 실패 후 처리
                            response.sendRedirect("/loginFailure");
                        })
                );

        return http.build();
    }
}