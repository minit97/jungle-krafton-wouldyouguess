package com.krafton.api_server.config;

import com.krafton.api_server.api.auth.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final KakaoService kakaoService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")) // h2
                        .disable())
                .headers(headers -> headers.frameOptions().disable()) // h2
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/login/**", "/h2-console/**").permitAll() // h2
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(kakaoService)
                        )
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/loginSuccess");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.sendRedirect("/loginFailure");
                        })
                )
                .sessionManagement(session -> session
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
                )
                .logout(logout -> logout
                        .logoutUrl("/")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}